package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.model.PontoColeta;
import br.com.fiap.esg_residuos.model.dto.PontoColetaCadastroDTO;
import br.com.fiap.esg_residuos.repository.PontoColetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PontoColetaServiceTest {

    @Mock
    private PontoColetaRepository pontoColetaRepository;

    @InjectMocks
    private PontoColetaService pontoColetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarShouldSaveAndReturnDTO() {
        PontoColeta salvo = new PontoColeta();
        salvo.setId(1L);
        salvo.setNome("Ponto Teste");
        salvo.setCidade("Cidade");
        salvo.setTipo("Papel");
        salvo.setVolumeMaximo(BigDecimal.valueOf(100));

        when(pontoColetaRepository.save(any(PontoColeta.class))).thenReturn(salvo);

        PontoColetaCadastroDTO dto = new PontoColetaCadastroDTO("Ponto Teste", "Endereco", "Cidade", "Papel", BigDecimal.valueOf(100));
        var result = pontoColetaService.cadastrar(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(pontoColetaRepository, times(1)).save(any(PontoColeta.class));
    }

    @Test
    void buscarPorIdWhenNotFoundShouldThrow() {
        when(pontoColetaRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> pontoColetaService.buscarPorId(99L));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void listarShouldReturnByCidade() {
        PontoColeta p = new PontoColeta();
        p.setId(1L);
        p.setNome("A");
        p.setCidade("CidadeX");
        p.setTipo("Papel");

        when(pontoColetaRepository.findByCidade("CidadeX")).thenReturn(List.of(p));

        var list = pontoColetaService.listar("CidadeX", null);
        assertEquals(1, list.size());
        assertEquals("CidadeX", list.get(0).cidade());
    }

    @Test
    void atualizarShouldUpdateWhenExists() {
        PontoColeta existing = new PontoColeta();
        existing.setId(2L);
        existing.setNome("Old");
        existing.setCidade("C1");

        when(pontoColetaRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(pontoColetaRepository.save(any(PontoColeta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PontoColetaCadastroDTO dto = new PontoColetaCadastroDTO("New Name", "End", "C1", "Vidro", BigDecimal.valueOf(10));
        var result = pontoColetaService.atualizar(2L, dto);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("New Name", result.nome());
    }

    @Test
    void removerWhenNotFoundShouldThrow() {
        when(pontoColetaRepository.existsById(99L)).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> pontoColetaService.remover(99L));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }
}
