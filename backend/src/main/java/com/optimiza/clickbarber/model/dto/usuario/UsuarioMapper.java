package com.optimiza.clickbarber.model.dto.usuario;

import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioCadastrarDto usuarioRegistrar) {
        return Usuario.builder()
                .email(usuarioRegistrar.getEmail())
                .senha(usuarioRegistrar.getSenha())
                .role(usuarioRegistrar.getRole())
                .build();
    }

    public UsuarioAgendamentoDto toAgendamentoDto(Usuario usuario) {
        return UsuarioAgendamentoDto.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .build();
    }

}
