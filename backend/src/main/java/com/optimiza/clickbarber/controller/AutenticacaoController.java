package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.RespostaAutenticacao;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
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

        return respostaLogin.isSuccess() ? ResponseEntity.ok(respostaLogin) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respostaLogin);
    }

}
