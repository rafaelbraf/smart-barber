package com.optimiza.clickbarber.service

import com.optimiza.clickbarber.exception.ResourceNotFoundException
import com.optimiza.clickbarber.model.Cliente
import com.optimiza.clickbarber.model.dto.cliente.ClienteCadastroDto
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto
import com.optimiza.clickbarber.model.dto.cliente.ClienteMapper
import com.optimiza.clickbarber.repository.ClienteRepository
import com.optimiza.clickbarber.utils.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClienteService @Autowired constructor(
    private val clienteRepository: ClienteRepository,
    private val clienteMapper: ClienteMapper
) {
    fun buscarPorUsuarioId(usuarioId: Long): ClienteDto {
        val clienteEncontrado = clienteRepository.findByUsuarioId(usuarioId)
            .orElseThrow { ResourceNotFoundException(Constants.Entity.CLIENTE, Constants.Attribute.USUARIO_ID, usuarioId.toString()) }
        return clienteMapper.toDto(clienteEncontrado)
    }

    fun buscarPorId(id: Long): Cliente {
        return clienteRepository.findById(id).orElseThrow { ResourceNotFoundException(Constants.Entity.CLIENTE, Constants.Attribute.ID, id.toString()) }
    }

    fun buscarPorIdExternoBarbearia(idExternoBarbearia: UUID): List<ClienteDto> {
        return clienteRepository.findByIdExternoBarbearia(idExternoBarbearia)
            .map(clienteMapper::toDto)
    }

    fun cadastrar(usuarioCadastro: ClienteCadastroDto?): ClienteDto {
        val cliente = clienteMapper.toEntity(usuarioCadastro)
        val clienteCadastrado = clienteRepository.save(cliente)
        return clienteMapper.toDto(clienteCadastrado)
    }
}