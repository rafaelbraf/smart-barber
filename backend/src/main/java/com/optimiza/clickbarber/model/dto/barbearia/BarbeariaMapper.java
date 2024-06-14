package com.optimiza.clickbarber.model.dto.barbearia;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class BarbeariaMapper {

    public BarbeariaDto toDto(Barbearia barbearia) {
        if (isNull(barbearia)) return null;

        return BarbeariaDto.builder()
                .idExterno(barbearia.getIdExterno())
                .nome(barbearia.getNome())
                .cnpj(barbearia.getCnpj())
                .telefone(barbearia.getTelefone())
                .endereco(barbearia.getEndereco())
                .build();
    }

    public Barbearia toEntity(BarbeariaCadastroDto barbeariaCadastro) {
        if (isNull(barbeariaCadastro)) return null;

        return Barbearia.builder()
                .nome(barbeariaCadastro.getNome())
                .cnpj(barbeariaCadastro.getCnpj())
                .endereco(barbeariaCadastro.getEndereco())
                .telefone(barbeariaCadastro.getTelefone())
                .usuario(barbeariaCadastro.getUsuario())
                .build();
    }

    public Barbearia toEntity(BarbeariaDto barbearia) {
        if (isNull(barbearia)) return null;

        return Barbearia.builder()
                .nome(barbearia.getNome())
                .cnpj(barbearia.getCnpj())
                .endereco(barbearia.getEndereco())
                .telefone(barbearia.getTelefone())
                .build();
    }

}
