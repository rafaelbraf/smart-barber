package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Cliente;
import com.optimiza.clickbarber.model.dto.cliente.ClienteCadastroDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteMapper;
import com.optimiza.clickbarber.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static com.optimiza.clickbarber.utils.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    private Long usuarioId;
    private UUID idExternoCliente;

    @BeforeEach
    void setup() {
        usuarioId = 1L;
        idExternoCliente = UUID.randomUUID();
    }

    @Test
    void testBuscarClientePorUsuarioId_Encontrado() {
        var cliente = montarCliente();
        when(clienteRepository.findByUsuarioId(anyLong())).thenReturn(Optional.of(cliente));

        var clienteDto = montarClienteDto(idExternoCliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

        var clienteEncontradoResult = clienteService.buscarPorUsuarioId(usuarioId);
        assertNotNull(clienteEncontradoResult);
        assertEquals(1, cliente.getId());
        assertEquals("Cliente Teste", cliente.getNome());
    }

    @Test
    void testBuscarClientePorUsuarioId_NaoEncontrado() {
        when(clienteRepository.findByUsuarioId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorUsuarioId(usuarioId));
    }

    @Test
    void testBuscarClientePorId_Encontrado() {
        var cliente = montarCliente();
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        var clienteEncontradoResult = clienteService.buscarPorId(1L);
        assertNotNull(clienteEncontradoResult);
        assertEquals(1, clienteEncontradoResult.getId());
        assertEquals("Cliente Teste", clienteEncontradoResult.getNome());
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(1L));
    }

    @Test
    void testCadastrarCliente() {
        var cliente = montarCliente();
        when(clienteMapper.toEntity(any(ClienteCadastroDto.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var clienteDto = montarClienteDto(idExternoCliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

        var clienteCadastroDto = montarClienteCadastroDto();
        var clienteCadastradoResult = clienteService.cadastrar(clienteCadastroDto);
        assertNotNull(clienteCadastradoResult);
        assertEquals(idExternoCliente, clienteCadastradoResult.getIdExterno());
        assertEquals("Cliente Teste", clienteCadastradoResult.getNome());
    }

}
