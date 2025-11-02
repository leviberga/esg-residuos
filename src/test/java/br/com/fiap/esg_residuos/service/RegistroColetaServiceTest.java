package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.model.PontoColeta;
import br.com.fiap.esg_residuos.model.RegistroColeta;
import br.com.fiap.esg_residuos.model.dto.RegistroColetaCadastroDTO;
import br.com.fiap.esg_residuos.repository.PontoColetaRepository;
import br.com.fiap.esg_residuos.repository.RegistroColetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistroColetaServiceTest {

    @Mock
    private RegistroColetaRepository registroColetaRepository;

    @Mock
    private PontoColetaRepository pontoColetaRepository;

    @InjectMocks
    private RegistroColetaService registroColetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarShouldSaveAndReturnDTO() {
        PontoColeta ponto = new PontoColeta();
        ponto.setId(1L);
        ponto.setNome("P1");

        when(pontoColetaRepository.findById(1L)).thenReturn(Optional.of(ponto));

        RegistroColeta saved = new RegistroColeta();
        saved.setId(1L);
        saved.setPontoColeta(ponto);
        saved.setVolumeColetado(BigDecimal.valueOf(10.5));

        when(registroColetaRepository.save(any(RegistroColeta.class))).thenReturn(saved);

        RegistroColetaCadastroDTO dto = new RegistroColetaCadastroDTO(1L, BigDecimal.valueOf(10.5));
        var result = registroColetaService.registrar(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1L, result.pontoColetaId());
        verify(registroColetaRepository, times(1)).save(any(RegistroColeta.class));
    }

    @Test
    void registrarWhenPontoNotFoundShouldThrow() {
        when(pontoColetaRepository.findById(99L)).thenReturn(Optional.empty());
        RegistroColetaCadastroDTO dto = new RegistroColetaCadastroDTO(99L, BigDecimal.valueOf(5));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> registroColetaService.registrar(dto));
        assertTrue(ex.getMessage().contains("Ponto de coleta não encontrado"));
    }
}
