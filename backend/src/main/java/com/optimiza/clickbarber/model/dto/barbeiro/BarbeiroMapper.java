package com.optimiza.clickbarber.model.dto.barbeiro;

import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BarbeiroMapper {

    private final BarbeariaMapper barbeariaMapper;

    @Autowired
    public BarbeiroMapper(BarbeariaMapper barbeariaMapper) {
        this.barbeariaMapper = barbeariaMapper;
    }

    public Barbeiro toEntity(BarbeiroCadastroDto barbeiroCadastro) {
        var barbearia = barbeariaMapper.toEntity(barbeiroCadastro.getBarbearia());

        return Barbeiro.builder()
                .nome(barbeiroCadastro.getNome())
                .cpf(barbeiroCadastro.getCpf())
                .celular(barbeiroCadastro.getCelular())
                .ativo(barbeiroCadastro.isAtivo())
                .admin(barbeiroCadastro.isAdmin())
                .barbearia(barbearia)
                .usuario(barbeiroCadastro.getUsuario())
                .build();
    }

    public BarbeiroDto toDto(Barbeiro barbeiro) {
        return BarbeiroDto.builder()
                .id(barbeiro.getId())
                .nome(barbeiro.getNome())
                .cpf(barbeiro.getCpf())
                .celular(barbeiro.getCelular())
                .admin(barbeiro.isAdmin())
                .ativo(barbeiro.isAtivo())
                .barbearia(barbeiro.getBarbearia())
                .build();
    }

}
