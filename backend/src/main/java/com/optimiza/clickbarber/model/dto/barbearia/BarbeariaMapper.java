package com.optimiza.clickbarber.model.dto.barbearia;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class BarbeariaMapper {

    public BarbeariaDto toDto(Barbearia barbearia) {
        if (isNull(barbearia)) return null;

        return BarbeariaDto.builder()
                .id(barbearia.getId())
                .nome(barbearia.getNome())
                .cnpj(barbearia.getCnpj())
                .email(barbearia.getEmail())
                .telefone(barbearia.getTelefone())
                .endereco(barbearia.getEndereco())
                .build();
    }

    public BarbeariaDto toDto(BarbeariaCadastroDto barbeariaCadastro) {
        if (isNull(barbeariaCadastro)) return null;

        return BarbeariaDto.builder()
                .nome(barbeariaCadastro.getNome())
                .cnpj(barbeariaCadastro.getCnpj())
                .email(barbeariaCadastro.getEmail())
                .telefone(barbeariaCadastro.getTelefone())
                .endereco(barbeariaCadastro.getEndereco())
                .build();
    }

    public Barbearia toEntity(BarbeariaCadastroDto barbeariaCadastro) {
        if (isNull(barbeariaCadastro)) return null;

        return Barbearia.builder()
                .nome(barbeariaCadastro.getNome())
                .cnpj(barbeariaCadastro.getCnpj())
                .email(barbeariaCadastro.getEmail())
                .senha(barbeariaCadastro.getSenha())
                .endereco(barbeariaCadastro.getEndereco())
                .telefone(barbeariaCadastro.getTelefone())
                .build();
    }

    public Barbearia toEntity(BarbeariaDto barbeariaDto) {
        if (isNull(barbeariaDto)) return null;

        return Barbearia.builder()
                .nome(barbeariaDto.getNome())
                .cnpj(barbeariaDto.getCnpj())
                .email(barbeariaDto.getEmail())
                .endereco(barbeariaDto.getEndereco())
                .telefone(barbeariaDto.getTelefone())
                .build();
    }

}
