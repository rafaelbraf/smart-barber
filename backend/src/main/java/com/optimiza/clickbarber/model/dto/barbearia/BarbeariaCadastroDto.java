package com.optimiza.clickbarber.model.dto.barbearia;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class BarbeariaCadastroDto {

    private String cnpj;
    private String nome;
    private String endereco;
    private String email;
    private String senha;
    private String telefone;

}
