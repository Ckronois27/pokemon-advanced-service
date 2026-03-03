package com.prueba.pokemon_advanced_service.infrastructure;

import com.prueba.pokemon_advanced_service.application.PokemonFilterUseCase;
import com.prueba.pokemon_advanced_service.domain.PokemonFilteredResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de Entrada (Driving Adapter).
 * Expone los endpoints REST para que los clientes puedan filtrar Pokémon por tipo, defensa, peso y experiencia.
 */
@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    private final PokemonFilterUseCase pokemonFilterUseCase;

    public PokemonController(PokemonFilterUseCase pokemonFilterUseCase) {
        this.pokemonFilterUseCase = pokemonFilterUseCase;
    }

    // Filtrar por tipo elemental
    @GetMapping("/type/{type}")
    public ResponseEntity<PokemonFilteredResponse> getByType(@PathVariable String type) {
        return ResponseEntity.ok(pokemonFilterUseCase.filterByType(type));
    }

    // Filtrar por nivel mínimo de defensa
    @GetMapping("/defense")
    public ResponseEntity<PokemonFilteredResponse> getByMinDefense(
            @RequestParam(name = "min", defaultValue = "0") Integer min) {
        
        if (min < 0) throw new IllegalArgumentException("La defensa mínima no puede ser negativa.");
        
        return ResponseEntity.ok(pokemonFilterUseCase.filterByMinDefense(min));
    }

    // Filtrar por peso mínimo y máximo
    @GetMapping("/weight")
    public ResponseEntity<PokemonFilteredResponse> getByWeight(
            @RequestParam(name = "min") Integer min,
            @RequestParam(name = "max") Integer max) {
            
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Rango de peso inválido. Ambos deben ser positivos y min <= max.");
        }
            
        return ResponseEntity.ok(pokemonFilterUseCase.filterByWeight(min, max));
    }

    // Filtrar por experiencia base
    @GetMapping("/exp")
    public ResponseEntity<PokemonFilteredResponse> getByMinExperience(
            @RequestParam(name = "min", defaultValue = "0") Integer min) {
            
        if (min < 0) throw new IllegalArgumentException("La experiencia mínima no puede ser negativa.");
            
        return ResponseEntity.ok(pokemonFilterUseCase.filterByMinExperience(min));
    }
}