package com.prueba.pokemon_advanced_service.application;

import com.prueba.pokemon_advanced_service.domain.PokemonBasicInfo;
import com.prueba.pokemon_advanced_service.domain.PokemonFilteredResponse;
import com.prueba.pokemon_advanced_service.domain.PokemonRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de los Casos de Uso de filtrado.
 * Esta clase orquesta la lógica de negocio usando el Puerto de Salida para obtener datos,
 * sin saber absolutamente nada de Apache Camel o de cómo se consumen las APIs.
 */
@Service
public class PokemonFilterService implements PokemonFilterUseCase {

    private final PokemonRepositoryPort pokemonRepositoryPort;

    // Inyección de dependencias por constructor (La mejor práctica en Spring Boot)
    public PokemonFilterService(PokemonRepositoryPort pokemonRepositoryPort) {
        this.pokemonRepositoryPort = pokemonRepositoryPort;
    }

    @Override
    public PokemonFilteredResponse filterByType(String type) {
        // La PokeAPI ya nos devuelve la lista filtrada por tipo, así que solo la pedimos [cite: 8]
        List<PokemonBasicInfo> pokemons = pokemonRepositoryPort.getPokemonByType(type);
        return new PokemonFilteredResponse(pokemons.size(), pokemons);
    }

    @Override
    public PokemonFilteredResponse filterByMinDefense(Integer minDefense) {
        // Pedimos un lote de Pokémon y filtramos en memoria los que cumplan la condición [cite: 18, 19]
        List<PokemonBasicInfo> allPokemons = pokemonRepositoryPort.getPokemonBatch(50); // Lote de prueba
        
        List<PokemonBasicInfo> filtered = allPokemons.stream()
                .filter(p -> p.getStats() != null && p.getStats().getDefense() >= minDefense)
                .collect(Collectors.toList());
                
        return new PokemonFilteredResponse(filtered.size(), filtered);
    }

    @Override
    public PokemonFilteredResponse filterByWeight(Integer minWeight, Integer maxWeight) {
        List<PokemonBasicInfo> allPokemons = pokemonRepositoryPort.getPokemonBatch(50);
        
        List<PokemonBasicInfo> filtered = allPokemons.stream()
                .filter(p -> p.getWeight() != null && p.getWeight() >= minWeight && p.getWeight() <= maxWeight)
                .collect(Collectors.toList());
                
        return new PokemonFilteredResponse(filtered.size(), filtered);
    }

    @Override
    public PokemonFilteredResponse filterByMinExperience(Integer minExperience) {
        List<PokemonBasicInfo> allPokemons = pokemonRepositoryPort.getPokemonBatch(50);
        
        List<PokemonBasicInfo> filtered = allPokemons.stream()
                .filter(p -> p.getBaseExperience() != null && p.getBaseExperience() >= minExperience)
                .collect(Collectors.toList());
                
        return new PokemonFilteredResponse(filtered.size(), filtered);
    }
}