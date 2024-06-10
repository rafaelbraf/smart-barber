package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.*;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoCadastroDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoMapper;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoRespostaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAgendamentoDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import com.optimiza.clickbarber.repository.AgendamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private AgendamentoMapper agendamentoMapper;

    @Mock
    private BarbeariaService barbeariaService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ServicoService servicoService;

    @Mock
    private BarbeiroService barbeiroService;

    private UUID agendamentoId;
    private ZonedDateTime dataHoraAgendamento;
    private BigDecimal valorTotalAgendamento;

    @BeforeEach
    void setup() {
        agendamentoId = UUID.randomUUID();
        dataHoraAgendamento = ZonedDateTime.now();
        valorTotalAgendamento = new BigDecimal(50.0);
    }

    @Test
    void testBuscarAgendamentoPorId_Encontrado() {
        var agendamento = montarAgendamento();
        when(agendamentoRepository.findById(any(UUID.class))).thenReturn(Optional.of(agendamento));

        var agendamentoDto = montarAgendamentoDto();
        when(agendamentoMapper.toDto(any(Agendamento.class))).thenReturn(agendamentoDto);

        var agendamentoEncontrado = agendamentoService.buscarPorId(agendamentoId);
        assertNotNull(agendamentoEncontrado);
        assertEquals(agendamentoId, agendamentoEncontrado.getId());
        assertEquals(dataHoraAgendamento, agendamentoEncontrado.getDataHora());
        assertEquals(valorTotalAgendamento, agendamentoEncontrado.getValorTotal());
        assertEquals(45, agendamentoEncontrado.getTempoDuracaoEmMinutos());
        assertEquals(1, agendamentoEncontrado.getBarbearia().getId());
        assertEquals(1, agendamentoEncontrado.getCliente().getId());
        assertFalse(agendamentoEncontrado.getServicos().isEmpty());
        assertFalse(agendamentoEncontrado.getBarbeiros().isEmpty());
    }

    @Test
    void testBuscarAgendamentoPorId_NaoEncontrado() {
        when(agendamentoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agendamentoService.buscarPorId(UUID.randomUUID()));
    }

    @Test
    void testBuscarAgendamentosPorBarbeariaId() {
        var agendamento1 = montarAgendamento();
        var agendamento2 = montarAgendamento();
        var agendamentos = List.of(agendamento1, agendamento2);
        when(agendamentoRepository.findByBarbeariaId(anyInt())).thenReturn(agendamentos);

        var agendamentoRespostaDto = montarAgendamentoRespostaDto();
        when(agendamentoMapper.toAgendamentoRespostaDto(any(Agendamento.class))).thenReturn(agendamentoRespostaDto);

        var agendamentosLista = agendamentoService.buscarPorBarbeariaId(1);
        assertFalse(agendamentosLista.isEmpty());
        assertEquals(1, agendamentosLista.getFirst().getBarbearia().getId());
        assertEquals(1, agendamentosLista.getLast().getBarbearia().getId());
    }

    @Test
    void testBuscarAgendamentoPorBarbeariaIdEDataHora() {
        var agendamento1 = montarAgendamento();
        var agendamento2 = montarAgendamento();
        var agendamentos = List.of(agendamento1, agendamento2);
        when(agendamentoRepository.findByDataHoraAndBarbeariaId(any(ZonedDateTime.class), anyInt())).thenReturn(agendamentos);

        var agendamentosEncontrados = agendamentoService.buscarPorBarbeariaIdEDataHora(1, dataHoraAgendamento);
        assertFalse(agendamentosEncontrados.isEmpty());
        assertEquals(1, agendamentosEncontrados.getFirst().getBarbearia().getId());
        assertEquals(dataHoraAgendamento, agendamentosEncontrados.getFirst().getDataHora());
        assertEquals(1, agendamentosEncontrados.getLast().getBarbearia().getId());
        assertEquals(dataHoraAgendamento, agendamentosEncontrados.getLast().getDataHora());
    }

    @Test
    void testCadastrarAgendamento() {
        var barbearia = montarBarbearia();
        when(barbeariaService.buscarPorId(anyInt())).thenReturn(barbearia);

        var cliente = montarCliente();
        when(clienteService.buscarPorId(anyInt())).thenReturn(cliente);

        var servico = montarServico();
        when(servicoService.buscarPorId(any(UUID.class))).thenReturn(servico);

        var barbeiro = montarBarbeiro();
        when(barbeiroService.buscarPorId(anyInt())).thenReturn(barbeiro);

        var agendamento = montarAgendamento();
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        var agendamentoDto = montarAgendamentoDto();
        when(agendamentoMapper.toDto(any(Agendamento.class))).thenReturn(agendamentoDto);

        var agendamentoCadastroDto = montarAgendamentoCadastroDto();
        var agendamentoCadastrado = agendamentoService.cadastrar(agendamentoCadastroDto);
        assertNotNull(agendamentoCadastrado);
        assertEquals(dataHoraAgendamento, agendamentoCadastrado.getDataHora());
        assertEquals("50", agendamentoCadastrado.getValorTotal().toString());
        assertEquals(45, agendamentoCadastrado.getTempoDuracaoEmMinutos());
        assertEquals(1, agendamentoCadastrado.getBarbearia().getId());
        assertEquals(1, agendamentoCadastrado.getCliente().getId());
        assertFalse(agendamentoCadastrado.getServicos().isEmpty());
        assertFalse(agendamentoCadastrado.getBarbeiros().isEmpty());
    }

    @Test
    void testDeletarAgendamentoPorId() {
        agendamentoService.deletarPorId(agendamentoId);
        verify(agendamentoRepository, times(1)).deleteById(any(UUID.class));
    }

    private Agendamento montarAgendamento() {
        return Agendamento.builder()
                .id(agendamentoId)
                .dataHora(dataHoraAgendamento)
                .tempoDuracaoEmMinutos(45)
                .valorTotal(valorTotalAgendamento)
                .barbearia(montarBarbearia())
                .build();
    }

    private AgendamentoDto montarAgendamentoDto() {
        return AgendamentoDto.builder()
                .id(agendamentoId)
                .dataHora(dataHoraAgendamento)
                .tempoDuracaoEmMinutos(45)
                .valorTotal(valorTotalAgendamento)
                .barbearia(montarBarbeariaDto())
                .cliente(montarClienteDto())
                .servicos(Set.of(montarServico()))
                .barbeiros(Set.of(montarBarbeiroAgendamentoDto()))
                .build();
    }

    private AgendamentoCadastroDto montarAgendamentoCadastroDto() {
        return AgendamentoCadastroDto.builder()
                .tempoDuracaoEmMinutos(45)
                .dataHora(dataHoraAgendamento)
                .valorTotal(valorTotalAgendamento)
                .barbeariaId(1)
                .servicos(List.of(UUID.randomUUID()))
                .barbeiros(List.of(1))
                .clienteId(1)
                .build();
    }

    private AgendamentoRespostaDto montarAgendamentoRespostaDto() {
        return AgendamentoRespostaDto.builder()
                .id(agendamentoId)
                .dataHora(dataHoraAgendamento)
                .tempoDuracaoEmMinutos(45)
                .valorTotal(valorTotalAgendamento)
                .barbearia(montarBarbeariaDto())
                .cliente(montarClienteDto())
                .servicos(Set.of(montarServico()))
                .barbeiros(Set.of(montarBarbeiroAgendamentoDto()))
                .build();
    }

    private Barbearia montarBarbearia() {
        return Barbearia.builder()
                .id(1)
                .nome("Barbearia Teste")
                .cnpj("01234567891011")
                .telefone("988888888")
                .endereco("Rua Teste, 123")
                .build();
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .id(1)
                .nome("Barbearia Teste")
                .telefone("988888888")
                .endereco("Rua Teste, 123")
                .cnpj("01234567891011")
                .build();
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

    private Servico montarServico() {
        return Servico.builder()
                .id(UUID.randomUUID())
                .nome("Serviço Teste")
                .ativo(true)
                .preco(new BigDecimal("50.0"))
                .tempoDuracaoEmMinutos(45)
                .barbearia(montarBarbearia())
                .build();
    }

    private Barbeiro montarBarbeiro() {
        return Barbeiro.builder()
                .id(1)
                .nome("Barbeiro Teste")
                .cpf("012345678910")
                .ativo(true)
                .admin(false)
                .barbearia(montarBarbearia())
                .build();
    }

    private BarbeiroAgendamentoDto montarBarbeiroAgendamentoDto() {
        return BarbeiroAgendamentoDto.builder()
                .id(1)
                .cpf("012345678910")
                .nome("Barbeiro Teste")
                .admin(false)
                .ativo(true)
                .celular("988888888")
                .build();
    }

}
