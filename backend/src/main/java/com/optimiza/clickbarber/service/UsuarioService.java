package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioCadastroDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioMapper;
import com.optimiza.clickbarber.repository.UsuarioRepository;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public UsuarioDto buscarPorEmail(String email) {
        var usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.USUARIO, Constants.Attribute.EMAIL, email));

        return usuarioMapper.toDto(usuarioEncontrado);
    }

    public String buscarSenhaCriptografada(UUID id) {
        return usuarioRepository.findSenhaPorId(id);
    }

    public UsuarioDto cadastrar(UsuarioCadastroDto usuarioCadastro) {
        var senhaCriptografada = bCryptPasswordEncoder.encode(usuarioCadastro.getSenha());
        usuarioCadastro.setSenha(senhaCriptografada);

        var usuario = usuarioMapper.toEntity(usuarioCadastro);
        var usuarioCadastrado = usuarioRepository.save(usuario);

        return usuarioMapper.toDto(usuarioCadastrado);
    }

}
