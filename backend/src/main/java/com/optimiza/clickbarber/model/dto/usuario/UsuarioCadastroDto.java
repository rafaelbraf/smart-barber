package com.optimiza.clickbarber.model.dto.usuario;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UsuarioCadastroDto {

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String senha;
    private String celular;

}
