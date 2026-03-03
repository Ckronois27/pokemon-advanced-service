package com.prueba.pokemon_advanced_service.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  Entidad principal del dominio que representa la información básica de un Pokémon.
    Esta clase es agnóstica; no tiene dependencias de la PokeAPI ni de frameworks externos,
    cumpliendo con los principios de la Arquitectura Hexagonal.
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonBasicInfo {
    // Identificador único del Pokémon
    private Integer id;

    // Nombre del Pokémon
    private String name;

    // Lista de tipos a los que pertenece el Pokémon (e.g., "fire", "water")
    private List<String> types;

    // Peso del Pokémon
    private Integer weight;

    // Puntos de experiencia base que otorga el Pokémon
    private Integer baseExperience;

    // Estadísticas básicas del Pokémon (e.g., HP, Attack, Defense)
    private PokemonStats stats;
}
