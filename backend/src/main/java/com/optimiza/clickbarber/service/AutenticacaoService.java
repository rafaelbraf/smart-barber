package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.model.RespostaAutenticacao;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class AutenticacaoService {

    private final BarbeariaService barbeariaService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AutenticacaoService(BarbeariaService barbeariaService, JwtUtil jwtUtil) {
        this.barbeariaService = barbeariaService;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public RespostaAutenticacao<Object> loginBarbearia(LoginRequestDto loginRequest) {
        requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var barbearia = barbeariaService.buscarPorEmail(loginRequest.getEmail());
        var senhaBarbearia = barbeariaService.buscarSenha(barbearia.getId());
        var mensagem = "";
        if (isSenhaValida(loginRequest.getSenha(), senhaBarbearia)) {
            mensagem = "Login realizado com sucesso!";
            return montarRespostaAutenticacao(gerarToken(barbearia.getEmail()), true, HttpStatus.OK.value(), mensagem, barbearia);
        }

        mensagem = "Email ou senha incorreta!";

        return montarRespostaAutenticacao(null, false, HttpStatus.UNAUTHORIZED.value(), mensagem, null);
    }

    public BarbeariaDto cadastrarBarbearia(BarbeariaCadastroDto barbeariaCadastro) {
        return barbeariaService.cadastrar(barbeariaCadastro);
    }

    private String gerarToken(String email) {
        return jwtUtil.gerarToken(email);
    }

    private boolean isSenhaValida(String senha, String senhaCadastrada) {
        return bCryptPasswordEncoder.matches(senha, senhaCadastrada);
    }

    private RespostaAutenticacao<Object> montarRespostaAutenticacao(String accessToken, boolean success, int statusCode, String message, BarbeariaDto barbearia) {
        return RespostaAutenticacao.builder()
                .accessToken(accessToken)
                .success(success)
                .statusCode(statusCode)
                .message(message)
                .result(barbearia)
                .build();
    }

}
