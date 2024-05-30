package com.optimiza.clickbarber.model.dto.usuario;

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

}
