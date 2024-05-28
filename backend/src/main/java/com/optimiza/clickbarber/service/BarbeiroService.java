package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAtualizarDto;
import com.optimiza.clickbarber.repository.BarbeiroRepository;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeariaService barbeariaService;

    public BarbeiroService(BarbeiroRepository barbeiroRepository, BarbeariaService barbeariaService) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeariaService = barbeariaService;
    }

    public Barbeiro buscarPorId(Integer id) {
        return barbeiroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.BARBEIRO, Constants.Attribute.ID, id.toString()));
    }

    public List<Barbeiro> buscarPorBarbeariaId(Integer id) {
        return barbeiroRepository.findByBarbeariaId(id);
    }

    public Barbeiro cadastrar(Barbeiro barbeiro) {
        var barbeariaId = barbeiro.getBarbearia().getId();
        if (!isExisteBarbearia(barbeariaId)) {
            throw new ResourceNotFoundException(Constants.Entity.BARBEARIA, Constants.Attribute.ID, barbeariaId.toString());
        }
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro atualizar(BarbeiroAtualizarDto barbeiroAtualizar) {
        var barbeiroExistente = buscarPorId(barbeiroAtualizar.getId());
        barbeiroExistente.setNome(barbeiroAtualizar.getNome());
        barbeiroExistente.setAtivo(barbeiroAtualizar.isAtivo());
        barbeiroExistente.setAdmin(barbeiroAtualizar.isAdmin());
        barbeiroExistente.setCelular(barbeiroAtualizar.getCelular());
        return barbeiroRepository.save(barbeiroExistente);
    }

    public void deletarPorId(Integer id) {
        barbeiroRepository.deleteById(id);
    }

    private boolean isExisteBarbearia(Integer barbeariaId) {
        return barbeariaService.existePorId(barbeariaId);
    }

}
