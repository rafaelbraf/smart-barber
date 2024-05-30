package com.optimiza.clickbarber.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.model.RespostaLogin;
import com.optimiza.clickbarber.model.Role;
import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroCadastroDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteCadastroDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioCadastrarDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.*;

@Service
public class AutenticacaoService {

    private final BarbeariaService barbeariaService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
    private final ClienteService clienteService;
    private final BarbeiroService barbeiroService;

    @Autowired
    public AutenticacaoService(BarbeariaService barbeariaService, JwtUtil jwtUtil, UsuarioService usuarioService, ObjectMapper objectMapper, ClienteService clienteService, BarbeiroService barbeiroService) {
        this.barbeariaService = barbeariaService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.objectMapper = objectMapper;
        this.clienteService = clienteService;
        this.barbeiroService = barbeiroService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public RespostaLogin loginBarbearia(LoginRequestDto loginRequest) {
        requireNonNull(loginRequest.getEmail(), "Email não pode ser nulo.");
        requireNonNull(loginRequest.getSenha(), "Senha não pode ser nulo.");

        var usuario = usuarioService.buscarPorEmail(loginRequest.getEmail());
        if (isSenhaValida(loginRequest.getSenha(), usuario.getSenha())) {
            if (usuario.getRole().equals(Role.CLIENTE)) {
                var cliente = clienteService.buscarPorUsuarioId(usuario.getId());
                return RespostaLogin.authorized(cliente, gerarToken(usuario.getEmail()));
            } else if (usuario.getRole().equals(Role.BARBEARIA)) {
                var barbearia = barbeariaService.buscarPorUsuarioId(usuario.getId());
                return RespostaLogin.authorized(barbearia, gerarToken(usuario.getEmail()));
            } else if (usuario.getRole().equals(Role.BARBEIRO)) {
                var barbeiro = barbeiroService.buscarPorUsuarioId(usuario.getId());
                return RespostaLogin.authorized(barbeiro, gerarToken(usuario.getEmail()));
            }
        }

        return RespostaLogin.unauthorized();
    }

    @Transactional
    public Object cadastrarUsuario(UsuarioCadastrarDto usuarioRegistrar) {
        var usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioRegistrar);
        return cadastrarObjeto(usuarioRegistrar, usuarioCadastrado);
    }

    private String gerarToken(String email) {
        return jwtUtil.gerarToken(email);
    }

    private boolean isSenhaValida(String senha, String senhaCadastrada) {
        return bCryptPasswordEncoder.matches(senha, senhaCadastrada);
    }

    private Object cadastrarObjeto(UsuarioCadastrarDto usuarioRegistrar, Usuario usuarioCadastrado) {
        if (isNull(usuarioCadastrado)) return null;

        var data = usuarioRegistrar.getData();
        var role = usuarioRegistrar.getRole();

        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            if (role.equals(Role.BARBEARIA)) {
                var barbeariaCadastro = objectMapper.convertValue(dataMap, BarbeariaCadastroDto.class);
                barbeariaCadastro.setUsuario(usuarioCadastrado);

                return barbeariaService.cadastrar(barbeariaCadastro);
            } else if (role.equals(Role.CLIENTE)) {
                var clienteCadastro = objectMapper.convertValue(dataMap, ClienteCadastroDto.class);
                clienteCadastro.setUsuario(usuarioCadastrado);

                return clienteService.cadastrar(clienteCadastro);
            } else if (role.equals(Role.BARBEIRO)) {
                var barbeiroCadastro = objectMapper.convertValue(dataMap, BarbeiroCadastroDto.class);
                barbeiroCadastro.setUsuario(usuarioCadastrado);

                return barbeiroService.cadastrar(barbeiroCadastro);
            }
        }

        return null;
    }

}
