package com.prueba.pokemon_advanced_service.domain;
/*
   Puerto de Salida (Output Port) en la Arquitectura Hexagonal.
   Define el contrato estricto que la capa de Infraestructura (nuestro adaptador de Apache Camel)
   debe implementar para proveerle los datos de la PokeAPI a nuestra aplicación.
   * El dominio dicta las reglas, la infraestructura obedece.
 */

import java.util.List;

public interface PokemonRepositoryPort {
    /*  Método para obtener una lista de Pokémon filtrados por su tipo (e.g., "fire", "water").
        @pram type: El tipo de Pokémon por el cual se desea filtrar.
        @return: Una lista de objetos PokemonBasicInfo que cumplen con el criterio de tipo.
    */
    List<PokemonBasicInfo> getPokemonByType(String type);

    /* Método para obtener los detalles de un Pokémon específico por su nombre.
        se utilizara para extraer las estadísticas de combate necesarias para el filtro de "armadura" o defensa.
        @param name: El nombre del Pokémon del cual se desean obtener los detalles.
        @return: Un objeto PokemonBasicInfo con la información detallada del Pokémon solicitado.
     */
    PokemonBasicInfo getPokemonDetailsByName(String name);

    /* 
       Obtiene un lote general de Pokémon para cuando necesitemos aplicar los filtros
       numéricos globales (defensa, peso, experiencia)[cite: 16, 22, 24].
       @param limit: La cantidad máxima de Pokémon a obtener en este lote (e.g., 100).
       @return: Una lista de objetos PokemonBasicInfo con la información básica de cada Pokémon.
     */
    List<PokemonBasicInfo> getPokemonBatch(int limit);

}
