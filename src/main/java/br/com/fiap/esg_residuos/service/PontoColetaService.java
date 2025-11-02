package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.model.PontoColeta;
import br.com.fiap.esg_residuos.model.dto.PontoColetaCadastroDTO;
import br.com.fiap.esg_residuos.model.dto.PontoColetaExibicaoDTO;
import br.com.fiap.esg_residuos.repository.PontoColetaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PontoColetaService {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Transactional
    public PontoColetaExibicaoDTO cadastrar(PontoColetaCadastroDTO pontoColetaDTO) {
        PontoColeta pontoColeta = new PontoColeta();
        BeanUtils.copyProperties(pontoColetaDTO, pontoColeta);

        PontoColeta pontoSalvo = pontoColetaRepository.save(pontoColeta);
        return new PontoColetaExibicaoDTO(pontoSalvo);
    }

    @Transactional(readOnly = true)
    public List<PontoColetaExibicaoDTO> listar(String cidade, String tipo) {

        // Implementação simples e segura usando métodos do repositório
        // Evitamos o uso incorreto de Specification/Sort e utilizamos os métodos já disponíveis.
        if ((cidade == null || cidade.isEmpty()) && (tipo == null || tipo.isEmpty())) {
            return pontoColetaRepository.findAll().stream()
                    .map(PontoColetaExibicaoDTO::new)
                    .toList();
        }

        if (cidade != null && !cidade.isEmpty() && (tipo == null || tipo.isEmpty())) {
            return pontoColetaRepository.findByCidade(cidade).stream()
                    .map(PontoColetaExibicaoDTO::new)
                    .toList();
        }

        if ((cidade == null || cidade.isEmpty()) && tipo != null && !tipo.isEmpty()) {
            return pontoColetaRepository.findByTipo(tipo).stream()
                    .map(PontoColetaExibicaoDTO::new)
                    .toList();
        }

        // Ambos os filtros presentes
        return pontoColetaRepository.findByCidadeAndTipo(cidade, tipo).stream()
                .map(PontoColetaExibicaoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PontoColetaExibicaoDTO buscarPorId(Long id) {
        Optional<PontoColeta> pontoOpt = pontoColetaRepository.findById(id);
        if (pontoOpt.isEmpty()) {
            throw new RuntimeException("Ponto de coleta não encontrado com ID: " + id);
        }
        return new PontoColetaExibicaoDTO(pontoOpt.get());
    }

    @Transactional
    public PontoColetaExibicaoDTO atualizar(Long id, PontoColetaCadastroDTO pontoColetaDTO) {
        // Busca o ponto existente
        PontoColeta pontoExistente = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto de coleta não encontrado com ID: " + id));

        // Copia as propriedades do DTO para a entidade existente
        BeanUtils.copyProperties(pontoColetaDTO, pontoExistente);
        pontoExistente.setId(id); // Garante que o ID não seja perdido

        PontoColeta pontoAtualizado = pontoColetaRepository.save(pontoExistente);
        return new PontoColetaExibicaoDTO(pontoAtualizado);
    }

    @Transactional
    public void remover(Long id) {
        if (!pontoColetaRepository.existsById(id)) {
            throw new RuntimeException("Ponto de coleta não encontrado com ID: " + id);
        }
        // Nota: Se houver registros de coleta (filhos), a remoção pode falhar
        // dependendo da configuração do banco (CONSTRAINT).
        // A entidade PontoColeta tem 'CascadeType.ALL', então o JPA deve remover os filhos.
        pontoColetaRepository.deleteById(id);
    }

    // Lógica para GET /coletas/alertas
    @Transactional(readOnly = true)
    public List<PontoColetaExibicaoDTO> listarAlertas() {
        // Encontra todos os pontos
        return pontoColetaRepository.findAll().stream()
                // Filtra a lista na memória
                .filter(ponto -> {
                    // Soma o volume de todos os registros daquele ponto
                    BigDecimal volumeAtual = ponto.getRegistros().stream()
                            .map(registro -> registro.getVolumeColetado())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Compara com o volume máximo (se definido)
                    return ponto.getVolumeMaximo() != null &&
                            volumeAtual.compareTo(ponto.getVolumeMaximo()) >= 0;
                })
                // Converte para DTO
                .map(PontoColetaExibicaoDTO::new)
                .toList();
    }
}