package com.prueba.pokemon_advanced_service.application;

import com.prueba.pokemon_advanced_service.domain.PokemonBasicInfo;
import com.prueba.pokemon_advanced_service.domain.PokemonFilteredResponse;
import com.prueba.pokemon_advanced_service.domain.PokemonStats;
import com.prueba.pokemon_advanced_service.domain.PokemonRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonFilterUseCaseTest {

    // 1. Creamos un Puerto "falso" que no se conecta a internet
    @Mock
    private PokemonRepositoryPort pokemonRepositoryPort;

    // 2. Inyectamos ese puerto falso en tu Caso de Uso real
    @InjectMocks
   private PokemonFilterService pokemonFilterService;

    @Test
    void shouldFilterPokemonByMinDefenseCorrectly() {
        // --- GIVEN (Preparar los datos falsos) ---
        PokemonStats statsWeak = PokemonStats.builder().defense(50).build();
        PokemonBasicInfo pikachu = PokemonBasicInfo.builder().name("pikachu").stats(statsWeak).build();

        PokemonStats statsStrong = PokemonStats.builder().defense(100).build();
        PokemonBasicInfo blastoise = PokemonBasicInfo.builder().name("blastoise").stats(statsStrong).build();

        List<PokemonBasicInfo> mockList = List.of(pikachu, blastoise);

        // AQUÍ ESTÁ LA MAGIA: Le decimos al puerto falso qué responder cuando le pidan los datos
        // IMPORTANTE: Cambia "obtenerTodosLosPokemon()" por el nombre real de tu método en el puerto.
       when(pokemonRepositoryPort.getPokemonBatch(org.mockito.Mockito.anyInt())).thenReturn(mockList);

        // --- WHEN (Ejecutar tu código real) ---
        // Ahora sí, llamamos a tu método solo con el parámetro 80
        PokemonFilteredResponse result = pokemonFilterService.filterByMinDefense(80);

        // --- THEN (Verificar que tu lógica funcionó) ---
        assertEquals(1, result.getResults().size(), "El filtro debería dejar pasar solo a 1 Pokémon");
        assertEquals("blastoise", result.getResults().get(0).getName(), "El Pokémon filtrado debe ser Blastoise");
    }

    @Test
    void shouldFilterPokemonByMinExperienceCorrectly() {
        // --- GIVEN (Datos falsos) ---
        // Creamos un Pokémon con poca experiencia (50)
       PokemonStats statsLow = PokemonStats.builder().defense(50).build();
       PokemonBasicInfo pidgey = PokemonBasicInfo.builder()
        .name("pidgey")
        .baseExperience(50)
        .stats(statsLow)
        .build();

        // Creamos un Pokémon con mucha experiencia (150)
        PokemonStats statsHigh = PokemonStats.builder().defense(100).build();
        PokemonBasicInfo charizard = PokemonBasicInfo.builder()
        .name("charizard")
        .baseExperience(150) // <-- ¡Y también aquí!
        .stats(statsHigh)
        .build();

        List<PokemonBasicInfo> mockList = List.of(pidgey, charizard);

        // Le decimos al puerto falso qué responder (igual que en la prueba anterior)
        when(pokemonRepositoryPort.getPokemonBatch(org.mockito.Mockito.anyInt())).thenReturn(mockList);

        // --- WHEN (Ejecutamos el filtro pidiendo mínimo 100 de exp) ---
        PokemonFilteredResponse result = pokemonFilterService.filterByMinExperience(100);

        // --- THEN (Verificamos) ---
        assertEquals(1, result.getResults().size(), "El filtro debería dejar pasar solo a 1 Pokémon");
        assertEquals("charizard", result.getResults().get(0).getName(), "El Pokémon filtrado debe ser Charizard");
    }
}

