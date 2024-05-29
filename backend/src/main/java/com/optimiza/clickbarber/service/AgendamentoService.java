package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Agendamento;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoAtualizarDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoMapper;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoRespostaDto;
import com.optimiza.clickbarber.repository.AgendamentoRepository;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;

    @Autowired
    public AgendamentoService(AgendamentoRepository agendamentoRepository, AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
    }

    public AgendamentoDto buscarPorId(UUID id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.AGENDAMENTO, Constants.Attribute.ID, id.toString()));

        return agendamentoMapper.toDto(agendamento);
    }

    public List<AgendamentoRespostaDto> buscarPorBarbeariaId(Integer barbeariaId) {
        var agendamentos = agendamentoRepository.findByBarbeariaId(barbeariaId);
        var agendamentosRespostaDto = new ArrayList<AgendamentoRespostaDto>();

        for (Agendamento agendamento : agendamentos) {
            var agendamentoRespostaDto = agendamentoMapper.toAgendamentoRespostaDto(agendamento);
            agendamentosRespostaDto.add(agendamentoRespostaDto);
        }

        return agendamentosRespostaDto;
    }

    public List<Agendamento> buscarPorBarbeariaIdEDataHora(Integer barbeariaId, ZonedDateTime dataHora) {
        return agendamentoRepository.findByDataHoraAndBarbeariaId(dataHora, barbeariaId);
    }

    public Agendamento cadastrar(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public AgendamentoDto atualizar(AgendamentoAtualizarDto agendamento) {
        var agendamentoCadastrado = agendamentoRepository.findById(agendamento.getId())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.AGENDAMENTO, Constants.Attribute.ID, agendamento.getId().toString()));
        agendamentoCadastrado.setDataHora(agendamento.getDataHora());
        agendamentoCadastrado.setValorTotal(agendamento.getValorTotal());
        agendamentoCadastrado.setTempoDuracaoEmMinutos(agendamento.getTempoDuracaoEmMinutos());
        var agendamentoAtualizado = agendamentoRepository.save(agendamentoCadastrado);
        return agendamentoMapper.toDto(agendamentoAtualizado);
    }

    public void deletarPorId(UUID id) {
        agendamentoRepository.deleteById(id);
    }
}
