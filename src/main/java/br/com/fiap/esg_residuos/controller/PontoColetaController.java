package br.com.fiap.esg_residuos.controller;

import jakarta.validation.Valid;
import br.com.fiap.esg_residuos.model.dto.PontoColetaCadastroDTO;
import br.com.fiap.esg_residuos.model.dto.PontoColetaExibicaoDTO;
import br.com.fiap.esg_residuos.service.PontoColetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Prefixo comum para todos os endpoints da API
public class PontoColetaController {

    @Autowired
    private PontoColetaService pontoColetaService;

    // Endpoint 1: POST /ponto-coleta
    @PostMapping("/ponto-coleta")
    @ResponseStatus(HttpStatus.CREATED)
    public PontoColetaExibicaoDTO cadastrar(@RequestBody  @Valid PontoColetaCadastroDTO pontoColetaDTO) {
        return pontoColetaService.cadastrar(pontoColetaDTO);
    }

    // Endpoint 2: GET /pontos-coleta (com filtros)
    @GetMapping("/pontos-coleta")
    @ResponseStatus(HttpStatus.OK)
    public List<PontoColetaExibicaoDTO> listar(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String tipo
    ) {
        return pontoColetaService.listar(cidade, tipo);
    }

    // Endpoint 3: GET /ponto-coleta/{id}
    @GetMapping("/ponto-coleta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PontoColetaExibicaoDTO buscarPorId(@PathVariable Long id) {
        return pontoColetaService.buscarPorId(id);
    }

    // Endpoint 4: PUT /ponto-coleta/{id}
    @PutMapping("/ponto-coleta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PontoColetaExibicaoDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PontoColetaCadastroDTO pontoColetaDTO
    ) {
        return pontoColetaService.atualizar(id, pontoColetaDTO);
    }

    // Endpoint 5: DELETE /ponto-coleta/{id}
    @DeleteMapping("/ponto-coleta/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        pontoColetaService.remover(id);
    }

    // Endpoint 6 (Bônus): GET /coletas/alertas
    @GetMapping("/coletas/alertas")
    @ResponseStatus(HttpStatus.OK)
    public List<PontoColetaExibicaoDTO> listarAlertas() {
        return pontoColetaService.listarAlertas();
    }
}