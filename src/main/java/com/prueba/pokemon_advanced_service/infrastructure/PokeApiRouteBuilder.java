package com.prueba.pokemon_advanced_service.infrastructure;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * Enrutador de Apache Camel.
 * Define las rutas de integración, el manejo de errores HTTP y la orquestación de llamadas.
 */
@Component
public class PokeApiRouteBuilder extends RouteBuilder {

    private final PokemonTypeListProcessor typeListProcessor;
    private final PokemonDetailsProcessor detailsProcessor;

    // Inyectamos los procesadores que creaste en el paso anterior
    public PokeApiRouteBuilder(PokemonTypeListProcessor typeListProcessor, PokemonDetailsProcessor detailsProcessor) {
        this.typeListProcessor = typeListProcessor;
        this.detailsProcessor = detailsProcessor;
    }

    @Override
    public void configure() throws Exception {

        // 1. MANEJO DE ERRORES GLOBAL (Requisito de la prueba): 
        // Si la PokeAPI responde 404 (No Encontrado), atrapamos el error para que la app no explote.
        onException(HttpOperationFailedException.class)
            .onWhen(exchange -> {
                HttpOperationFailedException ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
                return ex.getStatusCode() == 404;
            })
            .handled(true)
            .log("Recurso no encontrado en la PokeAPI (Error 404)")
            .setBody(constant("")); // Devolvemos un cuerpo vacío para que el choice() de abajo lo maneje

        // 2. RUTA: Buscar por Tipo Elemental
        from("direct:getPokemonByType")
            .setHeader("CamelHttpMethod", constant("GET"))
            // Uso de toD() obligatorio para URLs dinámicas
            .toD("{{pokeapi.base-url}}type/${header.pokemonType}")
            .choice()
                .when(body().isNotEqualTo(""))
                    .process(typeListProcessor) // Transformamos el JSON gigante en nuestra lista
                .otherwise()
                    .setBody(constant(java.util.Collections.emptyList()))
            .end();

        // 3. RUTA: Buscar Detalles por Nombre de Pokémon
        from("direct:getPokemonByName")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD("{{pokeapi.base-url}}pokemon/${header.pokemonName}")
            .choice()
                .when(body().isNotEqualTo(""))
                    .process(detailsProcessor) // Extraemos la armadura, peso y exp
                .otherwise()
                    .setBody(constant((Object)null))
            .end();

        // Obtener un lote (Batch) y buscar los detalles de cada uno
        // Usamos el patrón "Splitter" de Camel.
        from("direct:getPokemonBatch")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD("{{pokeapi.base-url}}pokemon?limit=${header.batchLimit}")
            .unmarshal().json(org.apache.camel.model.dataformat.JsonLibrary.Jackson)
            .setBody(simple("${body[results]}")) // Extraemos el array de resultados
            // Por cada Pokémon en la lista, llamamos a la Ruta 3 para obtener sus estadísticas
            .split(body(), new org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy())
                .setHeader("pokemonName", simple("${body[name]}"))
                .to("direct:getPokemonByName") 
            .end();
    }
}