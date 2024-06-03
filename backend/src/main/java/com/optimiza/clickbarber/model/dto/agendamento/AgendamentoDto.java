package com.optimiza.clickbarber.model.dto.agendamento;

import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.Servico;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAgendamentoDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
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
    private ClienteDto cliente;
    private Set<Servico> servicos;
    private Set<BarbeiroAgendamentoDto> barbeiros;

}
