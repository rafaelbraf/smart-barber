package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.repository.BarbeariaRepository;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;
    private final BarbeariaMapper barbeariaMapper;

    @Autowired
    public BarbeariaService(BarbeariaRepository barbeariaRepository, BarbeariaMapper barbeariaMapper) {
        this.barbeariaRepository = barbeariaRepository;
        this.barbeariaMapper = barbeariaMapper;
    }

    public boolean existePorId(Integer id) {
        return barbeariaRepository.existsById(id);
    }

    public Barbearia buscarPorId(Integer id) {
        return barbeariaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.BARBEARIA, Constants.Attribute.ID, id.toString()));
    }

    public BarbeariaDto buscarPorUsuarioId(UUID usuarioId) {
        var barbearia = barbeariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.BARBEARIA, Constants.Attribute.USUARIO_ID, usuarioId.toString()));
        return barbeariaMapper.toDto(barbearia);
    }

    public BarbeariaDto cadastrar(BarbeariaCadastroDto barbeariaCadastro) {
        var barbearia = barbeariaMapper.toEntity(barbeariaCadastro);
        var barbeariaCadastrada = barbeariaRepository.save(barbearia);
        return barbeariaMapper.toDto(barbeariaCadastrada);
    }
}
