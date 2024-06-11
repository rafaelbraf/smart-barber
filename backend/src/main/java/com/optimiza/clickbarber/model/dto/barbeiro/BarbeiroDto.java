package com.optimiza.clickbarber.model.dto.barbeiro;

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class BarbeiroDto {

    private Long id;
    private String cpf;
    private String nome;
    private String celular;
    private boolean admin;
    private boolean ativo;
    private BarbeariaDto barbearia;

}
