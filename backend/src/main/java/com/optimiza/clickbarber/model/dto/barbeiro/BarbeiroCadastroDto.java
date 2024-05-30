package com.optimiza.clickbarber.model.dto.barbeiro;

import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class BarbeiroCadastroDto {

    private String nome;
    private String cpf;
    private String celular;
    private boolean admin;
    private boolean ativo;
    private BarbeariaDto barbearia;
    private Usuario usuario;

}
