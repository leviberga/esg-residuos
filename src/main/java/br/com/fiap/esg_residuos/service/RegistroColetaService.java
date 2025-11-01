package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.model.PontoColeta;
import br.com.fiap.esg_residuos.model.RegistroColeta;
import br.com.fiap.esg_residuos.model.dto.RegistroColetaCadastroDTO;
import br.com.fiap.esg_residuos.model.dto.RegistroColetaExibicaoDTO;
import br.com.fiap.esg_residuos.repository.PontoColetaRepository;
import br.com.fiap.esg_residuos.repository.RegistroColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistroColetaService {

    @Autowired
    private RegistroColetaRepository registroColetaRepository;

    @Autowired
    private PontoColetaRepository pontoColetaRepository; // Precisa para associar o registro

    @Transactional
    public RegistroColetaExibicaoDTO registrar(RegistroColetaCadastroDTO registroDTO) {

        // 1. Busca o Ponto de Coleta que receberá este registro
        Long pontoId = registroDTO.pontoColetaId();
        PontoColeta pontoColeta = pontoColetaRepository.findById(pontoId)
                .orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado com ID: " + pontoId));

        // 2. Cria a nova entidade de Registro
        RegistroColeta novoRegistro = new RegistroColeta();
        novoRegistro.setPontoColeta(pontoColeta);
        novoRegistro.setVolumeColetado(registroDTO.volumeColetado());
        // A data é definida automaticamente via @PrePersist na entidade

        // 3. Salva o registro
        RegistroColeta registroSalvo = registroColetaRepository.save(novoRegistro);

        // 4. Retorna o DTO de exibição
        return new RegistroColetaExibicaoDTO(registroSalvo);
    }
}