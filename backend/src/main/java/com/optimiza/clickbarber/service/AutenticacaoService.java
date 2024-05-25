package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.model.RespostaAutenticacao;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.repository.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AutenticacaoService {

    private final BarbeariaRepository barbeariaRepository;
    private final BarbeariaMapper barbeariaMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AutenticacaoService(BarbeariaRepository barbeariaRepository, BarbeariaMapper barbeariaMapper, JwtUtil jwtUtil) {
        this.barbeariaRepository = barbeariaRepository;
        this.barbeariaMapper = barbeariaMapper;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public RespostaAutenticacao<Object> loginBarbearia(LoginRequestDto loginRequest) {
        Objects.requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        Objects.requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var barbearia = barbeariaRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        if (isSenhaValida(loginRequest.getSenha(), barbearia.getSenha())) {
            var barbeariaDto = barbeariaMapper.toDto(barbearia);
            var mensagem = "Login realizado com sucesso!";

            return montarRespostaAutenticacao(gerarToken(barbeariaDto.getEmail()), true, HttpStatus.OK.value(), mensagem, barbeariaDto);
        }

        return montarRespostaAutenticacao(null, false, HttpStatus.UNAUTHORIZED.value(), "Email ou senha incorreta!", null);
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
