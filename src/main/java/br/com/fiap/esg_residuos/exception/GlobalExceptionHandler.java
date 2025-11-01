package br.com.fiap.esg_residuos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validação (ex: @NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Coleta todas as mensagens de erro das validações
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiErrorDTO errorDTO = new ApiErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    // Captura erros de "Não Encontrado" (que estamos lançando nos Serviços)
    @ExceptionHandler(RuntimeException.class) // Genérico; em um projeto maior, criaríamos uma exceção customizada
    public ResponseEntity<ApiErrorDTO> handleResourceNotFoundException(RuntimeException ex, HttpServletRequest request) {
        // Filtramos para os erros que criamos (ex: "Ponto de coleta não encontrado")
        if (ex.getMessage().contains("não encontrado")) {
            ApiErrorDTO errorDTO = new ApiErrorDTO(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Resource Not Found",
                    ex.getMessage(),
                    request.getRequestURI()
            );
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }

        // Se for outra RuntimeException, tratamos como erro interno
        return handleGenericException(ex, request);
    }

    // Captura genérica para qualquer outro erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        ApiErrorDTO errorDTO = new ApiErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}