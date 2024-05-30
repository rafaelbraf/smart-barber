package com.optimiza.clickbarber.model.dto.cliente;

import com.optimiza.clickbarber.model.Usuario;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ClienteCadastroDto {

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String celular;
    private Usuario usuario;

}
