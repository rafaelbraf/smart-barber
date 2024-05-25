package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.service.BarbeariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    private final BarbeariaService barbeariaService;

    @Autowired
    public BarbeariaController(BarbeariaService barbeariaService) {
        this.barbeariaService = barbeariaService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Resposta<BarbeariaDto>> findByEmail(@PathVariable String email) {
        try {
            var barbearia = barbeariaService.findByEmail(email);
            var resposta = montarResposta(HttpStatus.OK.value(), true, "Barbearia encontrada com sucesso!", barbearia);

            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var resposta = montarResposta(HttpStatus.NOT_FOUND.value(), false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }
    }

    @PostMapping
    public ResponseEntity<Resposta<BarbeariaDto>> cadastrarBarbearia(@RequestBody BarbeariaCadastroDto barbeariaCadastro) {
        try {
            var barbeariaCadastrada = barbeariaService.cadastrar(barbeariaCadastro);
            var resposta = montarResposta(HttpStatus.CREATED.value(), true, "Barbearia cadastrada com sucesso!", barbeariaCadastrada);

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(resposta);
        } catch (Exception e) {
            var resposta = montarResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resposta);
        }
    }

    private Resposta<BarbeariaDto> montarResposta(int statusCode, boolean success, String message, BarbeariaDto result) {
        return Resposta.<BarbeariaDto>builder()
                .statusCode(statusCode)
                .success(success)
                .message(message)
                .result(result)
                .build();
    }

}
