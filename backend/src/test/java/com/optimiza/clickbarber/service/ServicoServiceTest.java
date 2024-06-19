package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Servico;
import com.optimiza.clickbarber.model.dto.servico.ServicoDto;
import com.optimiza.clickbarber.model.dto.servico.ServicoMapper;
import com.optimiza.clickbarber.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.optimiza.clickbarber.utils.TestDataFactory.montarServico;
import static com.optimiza.clickbarber.utils.TestDataFactory.montarServicoDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ServicoServiceTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private ServicoMapper servicoMapper;

    @Mock
    private BarbeariaService barbeariaService;

    private Long servicoId;

    @BeforeEach
    void setup() {
        servicoId = 1L;
    }

    @Test
    void testBuscarTodosOsServicos() {
        var servico = montarServico();
        when(servicoRepository.findAll()).thenReturn(List.of(servico));

        var servicosEncontradosResult = servicoService.buscarTodos();
        assertFalse(servicosEncontradosResult.isEmpty());
        assertEquals("Serviço Teste", servicosEncontradosResult.getFirst().getNome());
        assertEquals(1, servicosEncontradosResult.getFirst().getBarbearia().getId());
    }

    @Test
    void testBuscarServicoPorId_Encontrado() {
        var servico = montarServico();
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.of(servico));

        var servicoEncontradoResult = servicoService.buscarPorId(1L);
        assertNotNull(servicoEncontradoResult);
        assertEquals("Serviço Teste", servicoEncontradoResult.getNome());
        assertEquals(1, servicoEncontradoResult.getBarbearia().getId());
    }

    @Test
    void testBuscarServicoPorId_NaoEncontrado() {
        when(servicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> servicoService.buscarPorId(servicoId));
    }

    @Test
    void testBuscarPorBarbeariaId() {
        var servico1 = montarServico();
        var servico2 = montarServico();
        var servicosLista = List.of(servico1, servico2);
        when(servicoRepository.findByBarbeariaId(anyInt())).thenReturn(servicosLista);

        var servicosEncontradosResult = servicoService.buscarPorBarbeariaId(1);
        assertFalse(servicosEncontradosResult.isEmpty());
        assertEquals(1, servicosEncontradosResult.getFirst().getBarbearia().getId());
        assertEquals(1, servicosEncontradosResult.getLast().getBarbearia().getId());
    }

    @Test
    void testCadastrarServico() {
        when(barbeariaService.existePorId(anyLong())).thenReturn(true);

        var servico = montarServico();
        when(servicoMapper.toEntity(any(ServicoDto.class))).thenReturn(servico);
        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

        var servicoDto = montarServicoDto();
        var servicoCadastradoResult = servicoService.cadastrar(servicoDto);
        assertNotNull(servicoCadastradoResult);
        assertEquals("Serviço Teste", servicoCadastradoResult.getNome());
        assertEquals("30.0", servicoCadastradoResult.getPreco().toString());
        assertEquals(30, servicoCadastradoResult.getTempoDuracaoEmMinutos());
        assertTrue(servicoCadastradoResult.isAtivo());
        assertEquals(1, servicoCadastradoResult.getBarbearia().getId());
    }

    @Test
    void testDeletarServicoPorId() {
        servicoService.deletarPorId(1L);
        verify(servicoRepository, times(1)).deleteById(anyLong());
    }

}
