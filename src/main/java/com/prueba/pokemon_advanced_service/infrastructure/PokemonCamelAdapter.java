package com.prueba.pokemon_advanced_service.infrastructure;

import com.prueba.pokemon_advanced_service.domain.PokemonBasicInfo;
import com.prueba.pokemon_advanced_service.domain.PokemonRepositoryPort;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonCamelAdapter implements PokemonRepositoryPort {
    
    private final ProducerTemplate producerTemplate;

    public PokemonCamelAdapter(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PokemonBasicInfo> getPokemonByType(String type) {
        // Enviamos un mensaje a la ruta Camel y esperamos la respuesta
        return producerTemplate.requestBodyAndHeader(
            "direct:getPokemonByType",
             null,
             "pokemonType",
             type,
             List.class);
    }

     @Override
    public PokemonBasicInfo getPokemonDetailsByName(String name){
        return producerTemplate.requestBodyAndHeader(
            "direct:getPokemonByName",
            null,
            "pokemonName",
            name,
            PokemonBasicInfo.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PokemonBasicInfo> getPokemonBatch(int limit){
        return producerTemplate.requestBodyAndHeader(
            "direct:getPokemonBatch",
            null,
            "batchLimit",
            limit,
            List.class);
    }
}
