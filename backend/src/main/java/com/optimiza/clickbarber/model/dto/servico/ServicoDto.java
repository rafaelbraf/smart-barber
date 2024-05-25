package com.optimiza.clickbarber.model.dto.servico;

import com.optimiza.clickbarber.model.Barbearia;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ServicoDto {

    @NotNull(message = "Nome não pode ser nulo")
    private String nome;

    @NotNull(message = "Preço não pode ser nulo")
    private BigDecimal preco;

    @NotNull(message = "Tempo duração em minutos não pode ser nulo")
    private Integer tempoDuracaoEmMinutos;

    @NotNull(message = "Barbearia não pode ser nula")
    private Barbearia barbearia;

    private boolean ativo;

}
