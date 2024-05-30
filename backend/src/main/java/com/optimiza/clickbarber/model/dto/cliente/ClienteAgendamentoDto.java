package com.optimiza.clickbarber.model.dto.cliente;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteAgendamentoDto {

    private Integer id;
    private String cpf;
    private String nome;
    private String email;
    private String celular;

}
