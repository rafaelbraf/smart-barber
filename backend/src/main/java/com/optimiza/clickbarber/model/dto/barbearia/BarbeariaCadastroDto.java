package com.optimiza.clickbarber.model.dto.barbearia;

import com.optimiza.clickbarber.model.Usuario;
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
    private String telefone;
    private Usuario usuario;

}
