package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaUtils;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAtualizarDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroCadastroDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroDto;
import com.optimiza.clickbarber.service.BarbeiroService;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    public BarbeiroController(BarbeiroService barbeiroService) {
        this.barbeiroService = barbeiroService;
    }

    @GetMapping("/{id}")
    public Resposta<Barbeiro> buscarPorId(@PathVariable Integer id) {
        var barbeiro = barbeiroService.buscarPorId(id);
        return RespostaUtils.ok(Constants.Success.BARBEIRO_ENCONTRADO_COM_SUCESSO, barbeiro);
    }

    @GetMapping("/barbearia/{id}")
    public Resposta<List<Barbeiro>> buscarPorBarbeariaId(@PathVariable Integer id) {
        var barbeiros = barbeiroService.buscarPorBarbeariaId(id);
        return RespostaUtils.ok(Constants.Success.BARBEIROS_ENCONTRADOS_DA_BARBEARIA, barbeiros);
    }

    @PostMapping
    public ResponseEntity<Resposta<BarbeiroDto>> cadastrar(@RequestBody BarbeiroCadastroDto barbeiroCadastro) {
        var barbeiroCadastrado = barbeiroService.cadastrar(barbeiroCadastro);
        var resposta = RespostaUtils.created(Constants.Success.BARBEIRO_CADASTRADO_COM_SUCESSO, barbeiroCadastrado);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping
    public Resposta<Barbeiro> atualizar(@RequestBody BarbeiroAtualizarDto barbeiroAtualizar) {
        var barbeiroAtualizado = barbeiroService.atualizar(barbeiroAtualizar);
        return RespostaUtils.ok(Constants.Success.BARBEIRO_ATUALIZADO_COM_SUCESSO, barbeiroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id) {
        barbeiroService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
