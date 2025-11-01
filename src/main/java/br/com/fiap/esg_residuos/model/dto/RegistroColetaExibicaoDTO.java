package br.com.fiap.esg_residuos.model.dto;

import br.com.fiap.esg_residuos.model.RegistroColeta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RegistroColetaExibicaoDTO(
        Long id,
        Long pontoColetaId,
        String nomePontoColeta,
        BigDecimal volumeColetado,
        LocalDateTime dataColeta
) {

    // Construtor que converte a Entidade (RegistroColeta) para este DTO
    public RegistroColetaExibicaoDTO(RegistroColeta registro) {
        this(
                registro.getId(),
                registro.getPontoColeta().getId(),
                registro.getPontoColeta().getNome(),
                registro.getVolumeColetado(),
                registro.getDataColeta()
        );
    }
}