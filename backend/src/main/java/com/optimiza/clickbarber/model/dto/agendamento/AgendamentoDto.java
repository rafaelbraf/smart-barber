package com.optimiza.clickbarber.model.dto.agendamento;

import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendamentoDto {

    private UUID id;
    private ZonedDateTime dataHora;
    private BigDecimal valorTotal;
    private Integer tempoDuracaoEmMinutos;
    private BarbeariaDto barbearia;
    private Cliente cliente;

}
