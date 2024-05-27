package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.dto.servico.ServicoAtualizarDto;
import com.optimiza.clickbarber.model.dto.servico.ServicoDto;
import com.optimiza.clickbarber.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    @Autowired
    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public ResponseEntity<Resposta<Object>> buscarTodos() {
        var servicos = servicoService.buscarTodos();
        var resposta = montarResposta(HttpStatus.OK.value(), true, "Serviços encontrados", servicos);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resposta<Object>> buscarPorId(@PathVariable UUID id) {
        var servico = servicoService.buscarPorId(id);
        var resposta = montarResposta(HttpStatus.OK.value(), true, "Serviço encontrado com sucesso!", servico);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/barbearia/{barbeariaId}")
    public ResponseEntity<Resposta<Object>> buscarPorBarbeariaId(@PathVariable Integer barbeariaId) {
        var servicos = servicoService.buscarPorBarbeariaId(barbeariaId);
        var resposta = montarResposta(HttpStatus.OK.value(), true, "Serviços encontrados para Barbearia com id " + barbeariaId, servicos);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Resposta<Object>> cadastrar(@RequestBody ServicoDto servico) {
        var servicoCadastrado = servicoService.cadastrar(servico);
        var resposta = montarResposta(HttpStatus.CREATED.value(), true, "Serviço cadastrado com sucesso!", servicoCadastrado);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(resposta);
    }

    @PutMapping
    public ResponseEntity<Resposta<Object>> atualizar(@RequestBody ServicoAtualizarDto servico) {
        var servicoAtualizado = servicoService.atualizar(servico);
        var resposta = montarResposta(HttpStatus.OK.value(), true, "Serviço atualizado com sucesso!", servicoAtualizado);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        servicoService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Resposta<Object> montarResposta(int statusCode, boolean success, String message, Object result) {
        return Resposta.<Object>builder()
                .statusCode(statusCode)
                .success(success)
                .message(message)
                .result(result)
                .build();
    }
}
