package com.optimiza.clickbarber.model.dto.agendamento;

import com.optimiza.clickbarber.model.Agendamento;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.model.dto.cliente.ClienteMapper;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class AgendamentoMapper {

    private final BarbeariaMapper barbeariaMapper;
    private final ClienteMapper clienteMapper;

    @Autowired
    public AgendamentoMapper(BarbeariaMapper barbeariaMapper, ClienteMapper clienteMapper) {
        this.barbeariaMapper = barbeariaMapper;
        this.clienteMapper = clienteMapper;
    }

    public AgendamentoDto toDto(Agendamento agendamento) {
        if (isNull(agendamento)) return null;

        var cliente = clienteMapper.toDto(agendamento.getCliente());
        var barbearia = barbeariaMapper.toDto(agendamento.getBarbearia());

        return AgendamentoDto.builder()
                .id(agendamento.getId())
                .dataHora(agendamento.getDataHora())
                .valorTotal(agendamento.getValorTotal())
                .tempoDuracaoEmMinutos(agendamento.getTempoDuracaoEmMinutos())
                .cliente(cliente)
                .barbearia(barbearia)
                .servicos(agendamento.getServicos())
                .build();
    }

    public AgendamentoRespostaDto toAgendamentoRespostaDto(Agendamento agendamento) {
        if (isNull(agendamento)) return null;

        var cliente = agendamento.getCliente();

        return AgendamentoRespostaDto.builder()
                .id(agendamento.getId())
                .dataHora(agendamento.getDataHora())
                .valorTotal(agendamento.getValorTotal())
                .tempoDuracaoEmMinutos(agendamento.getTempoDuracaoEmMinutos())
                .cliente(cliente)
                .build();
    }

}
