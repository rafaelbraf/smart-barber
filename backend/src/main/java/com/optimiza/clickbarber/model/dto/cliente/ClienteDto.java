package com.optimiza.clickbarber.model.dto.cliente;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDto {

    private Long id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String celular;

}
