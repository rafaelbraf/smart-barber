package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.model.Servico;
import com.optimiza.clickbarber.model.dto.servico.ServicoAtualizarDto;
import com.optimiza.clickbarber.model.dto.servico.ServicoDto;
import com.optimiza.clickbarber.model.dto.servico.ServicoMapper;
import com.optimiza.clickbarber.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    @Autowired
    public ServicoService(ServicoRepository servicoRepository, ServicoMapper servicoMapper) {
        this.servicoRepository = servicoRepository;
        this.servicoMapper = servicoMapper;
    }

    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }

    public Servico findById(UUID id) {
        return servicoRepository.findById(id).orElseThrow();
    }

    public List<Servico> findByBarbeariaId(Integer barbeariaId) {
        return servicoRepository.findByBarbeariaId(barbeariaId);
    }

    public Servico cadastrar(ServicoDto servicoDto) {
        var servico = servicoMapper.toEntity(servicoDto);
        return servicoRepository.save(servico);
    }

    public Servico atualizar(ServicoAtualizarDto servicoAtualizar) {
        var servico = servicoRepository.findById(servicoAtualizar.getId()).orElseThrow();
        servico.setNome(servicoAtualizar.getNome());
        servico.setAtivo(servicoAtualizar.isAtivo());
        servico.setPreco(servicoAtualizar.getPreco());
        servico.setTempoDuracaoEmMinutos(servicoAtualizar.getTempoDuracaoEmMinutos());

        return servicoRepository.save(servico);
    }

    public void deletarPorId(UUID id) {
        servicoRepository.deleteById(id);
    }
}
