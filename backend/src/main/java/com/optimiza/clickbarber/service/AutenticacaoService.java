package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.model.RespostaLogin;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioCadastroDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioDto;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class AutenticacaoService {

    private final BarbeariaService barbeariaService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioService usuarioService;

    @Autowired
    public AutenticacaoService(BarbeariaService barbeariaService, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.barbeariaService = barbeariaService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public RespostaLogin loginBarbearia(LoginRequestDto loginRequest) {
        requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var barbearia = barbeariaService.buscarPorEmail(loginRequest.getEmail());

        var senhaBarbearia = barbeariaService.buscarSenha(barbearia.getId());
        if (isSenhaValida(loginRequest.getSenha(), senhaBarbearia)) {
            return RespostaLogin.authorized(barbearia, gerarToken(barbearia.getEmail()));
        }

        return RespostaLogin.unauthorized();
    }

    public BarbeariaDto cadastrarBarbearia(BarbeariaCadastroDto barbeariaCadastro) {
        return barbeariaService.cadastrar(barbeariaCadastro);
    }

    public RespostaLogin loginUsuario(LoginRequestDto loginRequest) {
        requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var usuario = usuarioService.buscarPorEmail(loginRequest.getEmail());

        var senhaUsuario = usuarioService.buscarSenhaCriptografada(usuario.getId());
        if (isSenhaValida(loginRequest.getSenha(), senhaUsuario)) {
            return RespostaLogin.authorized(usuario, gerarToken(usuario.getEmail()));
        }

        return RespostaLogin.unauthorized();
    }

    public UsuarioDto cadastrarUsuario(UsuarioCadastroDto usuarioCadastro) {
        return usuarioService.cadastrar(usuarioCadastro);
    }

    private String gerarToken(String email) {
        return jwtUtil.gerarToken(email);
    }

    private boolean isSenhaValida(String senha, String senhaCadastrada) {
        return bCryptPasswordEncoder.matches(senha, senhaCadastrada);
    }

}
