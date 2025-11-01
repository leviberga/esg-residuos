package br.com.fiap.esg_residuos.exception;

import java.time.LocalDateTime;


public record ApiErrorDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}