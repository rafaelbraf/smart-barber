package com.optimiza.clickbarber.model.dto.agendamento;

import com.optimiza.clickbarber.model.Agendamento;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class AgendamentoMapper {

    private final UsuarioMapper usuarioMapper;
    private final BarbeariaMapper barbeariaMapper;

    @Autowired
    public AgendamentoMapper(UsuarioMapper usuarioMapper, BarbeariaMapper barbeariaMapper) {
        this.usuarioMapper = usuarioMapper;
        this.barbeariaMapper = barbeariaMapper;
    }

    public AgendamentoDto toDto(Agendamento agendamento) {
        if (isNull(agendamento)) return null;

        var usuario = usuarioMapper.toAgendamentoDto(agendamento.getUsuario());
        var barbearia = barbeariaMapper.toDto(agendamento.getBarbearia());

        return AgendamentoDto.builder()
                .id(agendamento.getId())
                .dataHora(agendamento.getDataHora())
                .valorTotal(agendamento.getValorTotal())
                .tempoDuracaoEmMinutos(agendamento.getTempoDuracaoEmMinutos())
                .usuario(usuario)
                .barbearia(barbearia)
                .build();
    }

    public AgendamentoRespostaDto toAgendamentoRespostaDto(Agendamento agendamento) {
        if (isNull(agendamento)) return null;

        var usuario = usuarioMapper.toAgendamentoDto(agendamento.getUsuario());

        return AgendamentoRespostaDto.builder()
                .id(agendamento.getId())
                .dataHora(agendamento.getDataHora())
                .valorTotal(agendamento.getValorTotal())
                .tempoDuracaoEmMinutos(agendamento.getTempoDuracaoEmMinutos())
                .usuario(usuario)
                .build();
    }

}
