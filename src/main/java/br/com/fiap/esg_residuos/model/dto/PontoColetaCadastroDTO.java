package br.com.fiap.esg_residuos.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;


public record PontoColetaCadastroDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        String endereco,

        @NotBlank(message = "A cidade é obrigatória.")
        String cidade,

        @NotBlank(message = "O tipo é obrigatório.")
        String tipo,

        @NotNull(message = "O volume máximo é obrigatório.")
        @Positive(message = "O volume máximo deve ser um número positivo.")
        BigDecimal volumeMaximo
) {
}