package com.optimiza.clickbarber.model.dto.barbearia;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarbeariaRespostaLoginDto {

    private UUID idExterno;
    private String cnpj;
    private String nome;
    private String endereco;
    private String telefone;

}
