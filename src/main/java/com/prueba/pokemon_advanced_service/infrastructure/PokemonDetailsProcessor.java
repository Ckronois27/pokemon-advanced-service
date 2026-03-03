package com.prueba.pokemon_advanced_service.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.pokemon_advanced_service.domain.PokemonBasicInfo;
import com.prueba.pokemon_advanced_service.domain.PokemonStats;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Procesador 2: Extrae los stats específicos (como la defensa/armadura),
 * peso y experiencia base de la respuesta detallada de la PokeAPI.
 */
@Component
public class PokemonDetailsProcessor implements Processor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        JsonNode rootNode = objectMapper.readTree(payload);

        Integer id = rootNode.path("id").asInt();
        String name = rootNode.path("name").asText();
        Integer weight = rootNode.path("weight").asInt();
        Integer baseExperience = rootNode.path("base_experience").asInt();

        // Extraer la lista de tipos
        List<String> types = new ArrayList<>();
        JsonNode typesArray = rootNode.path("types");
        if (typesArray.isArray()) {
            for (JsonNode typeNode : typesArray) {
                types.add(typeNode.path("type").path("name").asText());
            }
        }

        // Extraer específicamente la defensa ("armadura")
        Integer defense = 0;
        JsonNode statsArray = rootNode.path("stats");
        if (statsArray.isArray()) {
            for (JsonNode statNode : statsArray) {
                String statName = statNode.path("stat").path("name").asText();
                if ("defense".equals(statName)) {
                    defense = statNode.path("base_stat").asInt();
                    break;
                }
            }
        }

        // Construir nuestro modelo puro
        PokemonBasicInfo pokemon = PokemonBasicInfo.builder()
                .id(id)
                .name(name)
                .weight(weight)
                .baseExperience(baseExperience)
                .types(types)
                .stats(PokemonStats.builder().defense(defense).build())
                .build();

        exchange.getIn().setBody(pokemon);
    }
}