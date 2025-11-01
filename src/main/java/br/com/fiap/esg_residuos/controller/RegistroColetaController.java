package br.com.fiap.esg_residuos.controller;

import jakarta.validation.Valid;
import br.com.fiap.esg_residuos.model.dto.RegistroColetaCadastroDTO;
import br.com.fiap.esg_residuos.model.dto.RegistroColetaExibicaoDTO;
import br.com.fiap.esg_residuos.service.RegistroColetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Usando o mesmo prefixo
public class RegistroColetaController {

    @Autowired
    private RegistroColetaService registroColetaService;

    // Endpoint: POST /registro-coleta
    @PostMapping("/registro-coleta")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroColetaExibicaoDTO registrarColeta(@RequestBody @Valid RegistroColetaCadastroDTO registroDTO) {
        return registroColetaService.registrar(registroDTO);
    }
}