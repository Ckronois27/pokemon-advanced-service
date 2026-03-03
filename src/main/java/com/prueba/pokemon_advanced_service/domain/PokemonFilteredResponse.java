package com.prueba.pokemon_advanced_service.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
   Modelo de respuesta estructurada para devolver las consultas de la API.
   Encapsula los resultados para asegurar que el cliente reciba un formato estándar,
   independientemente del filtro aplicado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonFilteredResponse {

    // Cantidad total de pokemon encontrados tras aplicar los filtros
    private Integer total;

    // Lista de Pokémon que cumplen con los criterios de filtrado
    private List<PokemonBasicInfo> results;

}
