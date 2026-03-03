package com.prueba.pokemon_advanced_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad del dominio que encapsula las estadísticas de combate de un Pokémon.
 * Utilizada para aislar los atributos numéricos necesarios para los filtros de "armadura" o defensa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonStats {

    /** Nivel de defensa base ("armadura") del Pokémon */
    private Integer defense;
    
    // Nota: Se pueden agregar más estadísticas base (hp, attack, speed) si el negocio lo requiere a futuro.
    private Integer baseExperience;
}