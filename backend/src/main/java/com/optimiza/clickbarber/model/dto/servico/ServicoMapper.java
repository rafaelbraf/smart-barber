package com.optimiza.clickbarber.model.dto.servico;

import com.optimiza.clickbarber.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoDto servicoDto) {
        return Servico.builder()
                .nome(servicoDto.getNome())
                .preco(servicoDto.getPreco())
                .barbearia(servicoDto.getBarbearia())
                .tempoDuracaoEmMinutos(servicoDto.getTempoDuracaoEmMinutos())
                .ativo(servicoDto.isAtivo())
                .build();
    }

}
