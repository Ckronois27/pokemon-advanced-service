package com.prueba.pokemon_advanced_service.infrastructure;

import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador centralizado de errores.
 * Captura las excepciones lanzadas en cualquier parte de la aplicación 
 * y las transforma en respuestas HTTP limpias para el cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Valores numéricos fuera de rango o lógica inválida
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Valor fuera de rango o inválido", ex.getMessage());
    }

    // Tipos inválidos (ej. enviar letras donde va un número en la URL)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Tipo de dato inválido", "Asegúrate de enviar números donde corresponde.");
    }

    // Fallos en la PokeAPI (Errores 500 de su lado)
    @ExceptionHandler(HttpOperationFailedException.class)
    public ResponseEntity<Map<String, String>> handlePokeApiFailure(HttpOperationFailedException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, "Fallo en la PokeAPI", "La API externa respondió con error: " + ex.getStatusCode());
    }

    // Timeouts desde Camel (Si la PokeAPI tarda demasiado en responder)
    @ExceptionHandler(ExchangeTimedOutException.class)
    public ResponseEntity<Map<String, String>> handleCamelTimeout(ExchangeTimedOutException ex) {
        return buildResponse(HttpStatus.GATEWAY_TIMEOUT, "Timeout", "La PokeAPI tardó demasiado en responder.");
    }

    // Cualquier otro error no contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "Ocurrió un error inesperado.");
    }

    // Método auxiliar para construir el JSON de error
    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(status.value()));
        response.put("error", error);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}