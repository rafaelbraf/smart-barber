package com.optimiza.clickbarber.model.dto.agendamento;

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioAgendamentoDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioDto;
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
    private UsuarioAgendamentoDto usuario;

}
