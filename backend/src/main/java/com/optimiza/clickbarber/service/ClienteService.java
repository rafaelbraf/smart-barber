package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.dto.cliente.ClienteCadastroDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteMapper;
import com.optimiza.clickbarber.repository.ClienteRepository;
import com.optimiza.clickbarber.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteDto buscarPorUsuarioId(UUID usuarioId) {
        var clienteEncontrado = clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.Entity.CLIENTE, Constants.Attribute.USUARIO_ID, usuarioId.toString()));
        return clienteMapper.toDto(clienteEncontrado);
    }

    public ClienteDto cadastrar(ClienteCadastroDto usuarioCadastro) {
        var usuario = clienteMapper.toEntity(usuarioCadastro);
        var usuarioCadastrado = clienteRepository.save(usuario);
        return clienteMapper.toDto(usuarioCadastrado);
    }

}
