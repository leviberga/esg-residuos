package br.com.fiap.esg_residuos.model.dto;

import br.com.fiap.esg_residuos.model.PontoColeta;
import java.math.BigDecimal;

public record PontoColetaExibicaoDTO(
        Long id,
        String nome,
        String endereco,
        String cidade,
        String tipo,
        BigDecimal volumeMaximo
) {

    // Construtor que converte a Entidade (PontoColeta) para este DTO
    public PontoColetaExibicaoDTO(PontoColeta pontoColeta) {
        this(
                pontoColeta.getId(),
                pontoColeta.getNome(),
                pontoColeta.getEndereco(),
                pontoColeta.getCidade(),
                pontoColeta.getTipo(),
                pontoColeta.getVolumeMaximo()
        );
    }
}