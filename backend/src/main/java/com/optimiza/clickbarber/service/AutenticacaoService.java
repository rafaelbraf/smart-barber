package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.model.RespostaLogin;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RespostaLogin loginBarbearia(LoginRequestDto loginRequest) {
        requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var barbearia = barbeariaService.buscarPorEmail(loginRequest.getEmail());
        var senhaBarbearia = barbeariaService.buscarSenha(barbearia.getId());

        var mensagem = "Login realizado com sucesso!";
        if (!isSenhaValida(loginRequest.getSenha(), senhaBarbearia)) {
            mensagem = "Email ou senha incorreta!";
            return montarRespostaLogin(mensagem, false, null, null);
        }

        return montarRespostaLogin(mensagem, true, barbearia, gerarToken(barbearia.getEmail()));
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

    private RespostaLogin montarRespostaLogin(String message, boolean success, Object result, String accessToken) {
        return RespostaLogin.builder()
                .message(message)
                .result(result)
                .accessToken(accessToken)
                .success(success)
                .build();
    }

}
