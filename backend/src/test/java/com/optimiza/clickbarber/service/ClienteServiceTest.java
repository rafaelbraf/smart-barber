package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.dto.cliente.ClienteCadastroDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteMapper;
import com.optimiza.clickbarber.repository.ClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    private UUID usuarioId;

    @BeforeEach
    void setup() {
        usuarioId = UUID.randomUUID();
    }

    @Test
    void testBuscarClientePorUsuarioId_Encontrado() {
        var cliente = montarCliente();
        when(clienteRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.of(cliente));

        var clienteDto = montarClienteDto();
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

        var clienteEncontradoResult = clienteService.buscarPorUsuarioId(usuarioId);
        assertNotNull(clienteEncontradoResult);
        assertEquals(1, cliente.getId());
        assertEquals("Cliente Teste", cliente.getNome());
    }

    @Test
    void testBuscarClientePorUsuarioId_NaoEncontrado() {
        when(clienteRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorUsuarioId(usuarioId));
    }

    @Test
    void testBuscarClientePorId_Encontrado() {
        var cliente = montarCliente();
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(cliente));

        var clienteEncontradoResult = clienteService.buscarPorId(1);
        assertNotNull(clienteEncontradoResult);
        assertEquals(1, clienteEncontradoResult.getId());
        assertEquals("Cliente Teste", clienteEncontradoResult.getNome());
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(1));
    }

    @Test
    void testCadastrarCliente() {
        var cliente = montarCliente();
        when(clienteMapper.toEntity(any(ClienteCadastroDto.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clienteDto = montarClienteDto();
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

        var clienteCadastroDto = montarClienteCadastroDto();
        var clienteCadastradoResult = clienteService.cadastrar(clienteCadastroDto);
        assertNotNull(clienteCadastradoResult);
        assertEquals(1, clienteCadastradoResult.getId());
        assertEquals("Cliente Teste", clienteCadastradoResult.getNome());
    }

    private Cliente montarCliente() {
        return Cliente.builder()
                .id(1)
                .nome("Cliente Teste")
                .dataNascimento(LocalDate.of(2001, 1, 1))
                .cpf("012345678910")
                .celular("988888888")
                .build();
    }

    private ClienteDto montarClienteDto() {
        return ClienteDto.builder()
                .id(1)
                .dataNascimento(LocalDate.of(2001, 1, 1))
                .celular("988888888")
                .cpf("012345678910")
                .nome("Cliente Teste")
                .build();
    }

    private ClienteCadastroDto montarClienteCadastroDto() {
        return ClienteCadastroDto.builder()
                .cpf("012345678910")
                .nome("Cliente Teste")
                .celular("988888888")
                .dataNascimento(LocalDate.of(2001, 1, 1))
                .build();
    }

}
