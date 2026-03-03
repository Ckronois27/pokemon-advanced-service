package com.prueba.pokemon_advanced_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 /*
   Clase principal de arranque de la aplicación Spring Boot.
   Esta clase es el punto de entrada para ejecutar el servicio RESTful que expondrá los endpoints
   para consultar los Pokémon filtrados según los criterios definidos en el dominio.
   La anotación @SpringBootApplication habilita la configuración automática, el escaneo de componentes
   y otras características esenciales de Spring Boot.
  */
@SpringBootApplication
public class PokemonAdvancedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokemonAdvancedServiceApplication.class, args);
	}

}
