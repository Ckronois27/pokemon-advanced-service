package com.prueba.pokemon_advanced_service.application;

import com.prueba.pokemon_advanced_service.domain.PokemonFilteredResponse;

/* Puerto de Entrada (Input Port) en la Arquitectura Hexagonal.
    Define las operaciones que la capa de Aplicación (nuestro servicio de filtrado) expone
    para ser consumidas por los adaptadores de entrada (e.g., controladores REST).
    * La aplicación dicta las operaciones, los adaptadores de entrada obedecen.
 */
public interface PokemonFilterUseCase {

    PokemonFilteredResponse filterByType(String type);

    PokemonFilteredResponse filterByMinDefense(Integer minDefense);

    PokemonFilteredResponse filterByWeight(Integer minWeight, Integer maxWeight);

    PokemonFilteredResponse filterByMinExperience(Integer minExperience);

}
