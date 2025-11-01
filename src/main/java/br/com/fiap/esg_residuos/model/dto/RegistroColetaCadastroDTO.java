package br.com.fiap.esg_residuos.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RegistroColetaCadastroDTO(

        @NotNull(message = "O ID do ponto de coleta é obrigatório.")
        Long pontoColetaId,

        @NotNull(message = "O volume coletado é obrigatório.")
        @Positive(message = "O volume coletado deve ser um número positivo.")
        BigDecimal volumeColetado
) {
}