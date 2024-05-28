package com.optimiza.clickbarber.model.dto.usuario;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {

    private UUID id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String celular;

}
