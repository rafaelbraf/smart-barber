package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaUtils;
import com.optimiza.clickbarber.model.Servico;
import com.optimiza.clickbarber.model.dto.servico.ServicoAtualizarDto;
import com.optimiza.clickbarber.model.dto.servico.ServicoDto;
import com.optimiza.clickbarber.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Resposta<List<Servico>> buscarTodos() {
        var servicos = servicoService.buscarTodos();
        return RespostaUtils.ok("Serviços encontrados", servicos);
    }

    @GetMapping("/{id}")
    public Resposta<Servico> buscarPorId(@PathVariable UUID id) {
        var servicoEncontrado = servicoService.buscarPorId(id);
        return RespostaUtils.ok("Serviço encontrado com sucesso!", servicoEncontrado);
    }

    @GetMapping("/barbearia/{barbeariaId}")
    public Resposta<List<Servico>> buscarPorBarbeariaId(@PathVariable Integer barbeariaId) {
        var servicosEncontrados = servicoService.buscarPorBarbeariaId(barbeariaId);
        return RespostaUtils.ok("Serviços encontrados para Barbearia com id " + barbeariaId, servicosEncontrados);
    }

    @PostMapping
    public Resposta<Servico> cadastrar(@RequestBody ServicoDto servico) {
        var servicoCadastrado = servicoService.cadastrar(servico);
        return RespostaUtils.created("Serviço cadastrado com sucesso!", servicoCadastrado);
    }

    @PutMapping
    public Resposta<Servico> atualizar(@RequestBody ServicoAtualizarDto servicoAtualizar) {
        var servicoAtualizado = servicoService.atualizar(servicoAtualizar);
        return RespostaUtils.ok("Serviço atualizado com sucesso!", servicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public Resposta<Void> deletarPorId(@PathVariable UUID id) {
        servicoService.deletarPorId(id);
        return RespostaUtils.noContent();
    }

}
