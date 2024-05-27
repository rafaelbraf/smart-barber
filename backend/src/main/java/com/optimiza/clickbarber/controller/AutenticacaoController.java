package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaAutenticacao;
import com.optimiza.clickbarber.model.RespostaUtils;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @Autowired
    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/barbearias/login")
    public ResponseEntity<RespostaAutenticacao<Object>> loginBarbearia(@RequestBody LoginRequestDto loginRequest) {
        var respostaLogin = autenticacaoService.loginBarbearia(loginRequest);
        if (!respostaLogin.isSuccess()) {
            var resposta = RespostaUtils.unauthorized(respostaLogin.getMessage(), respostaLogin.getResult());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
        }

        var resposta = RespostaUtils.authorized(respostaLogin.getMessage(), respostaLogin.getResult(), respostaLogin.getAccessToken());
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/barbearias/cadastrar")
    public ResponseEntity<Resposta<BarbeariaDto>> cadastrarBarbearia(@RequestBody BarbeariaCadastroDto barbeariaCadastro) {
        var barbeariaCadastrada = autenticacaoService.cadastrarBarbearia(barbeariaCadastro);
        var resposta = RespostaUtils.created("Barbearia cadastrada com sucesso!", barbeariaCadastrada);

        return ResponseEntity.status(resposta.getStatusCode()).body(resposta);
    }

}
