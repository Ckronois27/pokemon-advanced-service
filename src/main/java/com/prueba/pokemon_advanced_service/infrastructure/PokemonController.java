package com.prueba.pokemon_advanced_service.infrastructure;

import com.prueba.pokemon_advanced_service.application.PokemonFilterUseCase;
import com.prueba.pokemon_advanced_service.domain.PokemonFilteredResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Swagger/OpenAPI Annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Adaptador de Entrada (Driving Adapter).
 * Expone los endpoints REST para que los clientes puedan filtrar Pokémon por tipo, defensa, peso y experiencia.
 */
@RestController
@RequestMapping("/api/v1/pokemon")
@Tag(name = "Filtros de Pokémon", description = "API Avanzada para filtrar Pokémon consumiendo la PokeAPI con Apache Camel.")
public class PokemonController {

    private final PokemonFilterUseCase pokemonFilterUseCase;

    public PokemonController(PokemonFilterUseCase pokemonFilterUseCase) {
        this.pokemonFilterUseCase = pokemonFilterUseCase;
    }

    // Filtrar por tipo elemental
    @Operation(summary = "Filtrar por tipo elemental", description = "Obtiene una lista de Pokémon que pertenecen a un tipo específico (ej. water, fire, electric).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista filtrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de Pokémon no encontrado", content = @Content)
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<PokemonFilteredResponse> getByType(
            @Parameter(description = "Tipo elemental del Pokémon", example = "water", required = true) 
            @PathVariable String type) {
        return ResponseEntity.ok(pokemonFilterUseCase.filterByType(type));
    }

    // Filtrar por nivel mínimo de defensa
    @Operation(summary = "Filtrar por defensa mínima", description = "Devuelve los Pokémon cuyo nivel de defensa base sea mayor o igual al valor especificado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista filtrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetro inválido (ej. número negativo)", content = @Content)
    })
    @GetMapping("/defense")
    public ResponseEntity<PokemonFilteredResponse> getByMinDefense(
            @Parameter(description = "Nivel mínimo de armadura/defensa", example = "80") 
            @RequestParam(name = "min", defaultValue = "0") Integer min) {
        
        if (min < 0) throw new IllegalArgumentException("La defensa mínima no puede ser negativa.");
        
        return ResponseEntity.ok(pokemonFilterUseCase.filterByMinDefense(min));
    }

    // Filtrar por peso mínimo y máximo
    @Operation(summary = "Filtrar por rango de peso", description = "Encuentra Pokémon que estén dentro del rango de peso especificado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista filtrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Rango de peso inválido", content = @Content)
    })
    @GetMapping("/weight")
    public ResponseEntity<PokemonFilteredResponse> getByWeight(
            @Parameter(description = "Peso mínimo permitido", example = "10", required = true) 
            @RequestParam(name = "min") Integer min,
            @Parameter(description = "Peso máximo permitido", example = "300", required = true) 
            @RequestParam(name = "max") Integer max) {
            
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Rango de peso inválido. Ambos deben ser positivos y min <= max.");
        }
            
        return ResponseEntity.ok(pokemonFilterUseCase.filterByWeight(min, max));
    }

    // Filtrar por experiencia base
    @Operation(summary = "Filtrar por experiencia base", description = "Busca Pokémon que entreguen una cantidad de experiencia base igual o superior a la indicada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista filtrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetro inválido (ej. número negativo)", content = @Content)
    })
    @GetMapping("/exp")
    public ResponseEntity<PokemonFilteredResponse> getByMinExperience(
            @Parameter(description = "Nivel mínimo de experiencia base", example = "100") 
            @RequestParam(name = "min", defaultValue = "0") Integer min) {
            
        if (min < 0) throw new IllegalArgumentException("La experiencia mínima no puede ser negativa.");
            
        return ResponseEntity.ok(pokemonFilterUseCase.filterByMinExperience(min));
    }
}