package com.optimiza.clickbarber.model.dto.usuario;

import com.optimiza.clickbarber.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCadastroDto usuarioCadastro) {
        return Usuario.builder()
                .nome(usuarioCadastro.getNome())
                .celular(usuarioCadastro.getCelular())
                .cpf(usuarioCadastro.getCpf())
                .email(usuarioCadastro.getEmail())
                .senha(usuarioCadastro.getSenha())
                .dataNascimento(usuarioCadastro.getDataNascimento())
                .build();
    }

    public UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .celular(usuario.getCelular())
                .cpf(usuario.getCpf())
                .email(usuario.getEmail())
                .dataNascimento(usuario.getDataNascimento())
                .build();
    }

}
