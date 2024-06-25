package com.optimiza.clickbarber.model.dto.barbeiro;

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class BarbeiroDto {

    private UUID idExterno;
    private String cpf;
    private String nome;
    private String celular;
    private boolean admin;
    private boolean ativo;

}
