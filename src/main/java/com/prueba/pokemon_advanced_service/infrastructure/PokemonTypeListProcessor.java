package com.prueba.pokemon_advanced_service.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.pokemon_advanced_service.domain.PokemonBasicInfo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Procesador 1: Transforma la respuesta de la PokeAPI al buscar por "Tipo"
 * en una lista de nuestros modelos de dominio.
 */
@Component
public class PokemonTypeListProcessor implements Processor {
    
    // ObjectMapper es la herramienta de la librería Jackson para leer JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Obtenemos el JSON gigante como texto
        String payload = exchange.getIn().getBody(String.class);
        
        // Lo convertimos en un árbol de nodos para navegarlo fácilmente
        JsonNode rootNode = objectMapper.readTree(payload);
        JsonNode pokemonArray = rootNode.path("pokemon");

        // Extraemos solo lo que nos sirve
        List<PokemonBasicInfo> pokemons = new ArrayList<>();
        if (pokemonArray.isArray()) {
            for (JsonNode node : pokemonArray) {
                String name = node.path("pokemon").path("name").asText();
                pokemons.add(PokemonBasicInfo.builder().name(name).build());
            }
        }
        
        // Reemplazamos el JSON gigante en el "Exchange" por nuestra lista limpia
        exchange.getIn().setBody(pokemons);
    }
}
