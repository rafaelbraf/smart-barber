package com.optimiza.clickbarber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.model.Servico;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoAtualizarDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoCadastroDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoDto;
import com.optimiza.clickbarber.model.dto.agendamento.AgendamentoRespostaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAgendamentoDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import com.optimiza.clickbarber.service.AgendamentoService;
import com.optimiza.clickbarber.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AgendamentoController.class, useDefaultFilters = false)
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendamentoService agendamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long agendamentoId;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AgendamentoController(agendamentoService)).build();

        agendamentoId = 1L;
    }

    @Test
    void testBuscarAgendamentoPorId() throws Exception {
        var barbearia = montarBarbeariaDto();
        var servico = montarServico();
        var barbeiro = montarBarbeiroAgendamentoDto();
        var cliente = montarClienteDto();
        var agendamentoEncontrado = montarAgendamentoDto(agendamentoId, barbearia, cliente, servico, barbeiro);
        when(agendamentoService.buscarPorId(anyLong())).thenReturn(agendamentoEncontrado);

        mockMvc.perform(get("/agendamentos/" + agendamentoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(Constants.Success.AGENDAMENTO_ENCONTRADO_COM_SUCESSO))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").value(agendamentoId.toString()))
                .andExpect(jsonPath("$.result.tempoDuracaoEmMinutos").value(45))
                .andExpect(jsonPath("$.result.cliente.nome").value("Cliente Teste"))
                .andExpect(jsonPath("$.result.barbearia.nome").value("Barbearia Teste"))
                .andExpect(jsonPath("$.result.servicos.[0].nome").value("Serviço Teste"))
                .andExpect(jsonPath("$.result.barbeiros.[0].nome").value("Barbeiro Teste"));
    }

    @Test
    void testBuscarAgendamentosPorBarbeariaId() throws Exception {
        var barbearia = montarBarbeariaDto();
        var servico = montarServico();
        var barbeiro = montarBarbeiroAgendamentoDto();
        var cliente = montarClienteDto();

        var agendamentoEncontrado1 = montarAgendamentoRespostaDto(1L, barbearia, cliente, servico, barbeiro);
        var agendamentoEncontrado2 = montarAgendamentoRespostaDto(2L, barbearia, cliente, servico, barbeiro);

        var agendamentosEncontradosList = List.of(agendamentoEncontrado1, agendamentoEncontrado2);

        when(agendamentoService.buscarPorBarbeariaId(anyInt())).thenReturn(agendamentosEncontradosList);

        mockMvc.perform(get("/agendamentos/barbearias/" + barbearia.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(Constants.Success.AGENDAMENTOS_ENCONTRADOS))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.[0].id").value("1"))
                .andExpect(jsonPath("$.result.[0].barbearia.id").value(barbearia.getId()))
                .andExpect(jsonPath("$.result.[1].id").value("2"))
                .andExpect(jsonPath("$.result.[1].barbearia.id").value(barbearia.getId()));
    }

    @Test
    void testCadastrarAgendamento() throws Exception {
        var dataHora = ZonedDateTime.now();
        var valorTotal = new BigDecimal("75.50");
        var tempoDuracaoEmMinutos = 45;

        var agendamentoCadastro = montarAgendamentoCadastroDto(valorTotal, tempoDuracaoEmMinutos, dataHora);

        var agendamentoDto = montarAgendamentoDto(agendamentoId, dataHora, tempoDuracaoEmMinutos, valorTotal);

        when(agendamentoService.cadastrar(any(AgendamentoCadastroDto.class))).thenReturn(agendamentoDto);

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamentoCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(Constants.Success.AGENDAMENTO_CADASTRADO_COM_SUCESSO))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").value(agendamentoId.toString()))
                .andExpect(jsonPath("$.result.tempoDuracaoEmMinutos").value(tempoDuracaoEmMinutos))
                .andExpect(jsonPath("$.result.barbearia.nome").value("Barbearia Teste"));
    }

    @Test
    void testAtualizarAgendamento() throws Exception {
        var dataHora = ZonedDateTime.now();
        var tempoDuracaoEmMinutos = 45;
        var valorTotal = new BigDecimal("75.70");

        var agendamentoAtualizado = montarAgendamentoDto(agendamentoId, dataHora, 30, new BigDecimal("40"));
        when(agendamentoService.atualizar(any(AgendamentoAtualizarDto.class))).thenReturn(agendamentoAtualizado);

        var agendamentoParaAtualizar = montarAgendamentoAtualizarDto(agendamentoId, dataHora, valorTotal, tempoDuracaoEmMinutos);

        mockMvc.perform(put("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamentoParaAtualizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constants.Success.AGENDAMENTO_ATUALIZADO_COM_SUCESSO))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").value(agendamentoId.toString()))
                .andExpect(jsonPath("$.result.tempoDuracaoEmMinutos").value(30))
                .andExpect(jsonPath("$.result.valorTotal").value("40"));
    }

    @Test
    void testDeletarAgendamentoPorId() throws Exception {
        doNothing().when(agendamentoService).deletarPorId(agendamentoId);

        mockMvc.perform(delete("/agendamentos/" + agendamentoId))
                .andExpect(status().isNoContent());
    }


    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
            .id(1L)
            .nome("Barbearia Teste")
            .cnpj("0123456789101112")
            .endereco("Rua Teste, 123")
            .telefone("988888888")
            .build();
    }

    private Servico montarServico() {
        return Servico.builder()
            .id(1L)
            .ativo(true)
            .nome("Serviço Teste")
            .preco(new BigDecimal("30.00"))
            .tempoDuracaoEmMinutos(30)
            .build();
    }

    private BarbeiroAgendamentoDto montarBarbeiroAgendamentoDto() {
        return BarbeiroAgendamentoDto.builder()
            .id(1L)
            .nome("Barbeiro Teste")
            .admin(false)
            .ativo(true)
            .cpf("012345678910")
            .celular("988888888")
            .build();
    }

    private ClienteDto montarClienteDto() {
        return ClienteDto.builder()
            .id(1L)
            .nome("Cliente Teste")
            .celular("988888888")
            .cpf("012345678910")
            .dataNascimento(LocalDate.of(2001, 1, 1))
            .build();
    }

    private AgendamentoDto montarAgendamentoDto(Long agendamentoId, ZonedDateTime dataHora, Integer tempoDuracaoEmMinutos, BigDecimal valorTotal) {
        return AgendamentoDto.builder()
                .id(agendamentoId)
                .cliente(montarClienteDto())
                .barbearia(montarBarbeariaDto())
                .servicos(Set.of(montarServico()))
                .barbeiros(Set.of(montarBarbeiroAgendamentoDto()))
                .dataHora(dataHora)
                .tempoDuracaoEmMinutos(tempoDuracaoEmMinutos)
                .valorTotal(valorTotal)
                .build();
    }

    private AgendamentoDto montarAgendamentoDto(Long agendamentoId, BarbeariaDto barbearia, ClienteDto cliente, Servico servico, BarbeiroAgendamentoDto barbeiro) {
        return AgendamentoDto.builder()
            .id(agendamentoId)
            .dataHora(ZonedDateTime.now())
            .tempoDuracaoEmMinutos(45)
            .valorTotal(new BigDecimal("75.50"))
            .barbearia(barbearia)
            .cliente(cliente)
            .servicos(Set.of(servico))
            .barbeiros(Set.of(barbeiro))
            .build();
    }

    private AgendamentoRespostaDto montarAgendamentoRespostaDto(Long agendamentoId, BarbeariaDto barbearia, ClienteDto cliente, Servico servico, BarbeiroAgendamentoDto barbeiro) {
        return AgendamentoRespostaDto.builder()
                .id(agendamentoId)
                .dataHora(ZonedDateTime.now())
                .tempoDuracaoEmMinutos(45)
                .valorTotal(new BigDecimal("75.50"))
                .barbearia(barbearia)
                .cliente(cliente)
                .servicos(Set.of(servico))
                .barbeiros(Set.of(barbeiro))
                .build();
    }

    private AgendamentoCadastroDto montarAgendamentoCadastroDto(BigDecimal valorTotal, Integer tempoDuracaoEmMinutos, ZonedDateTime dataHora) {
        return AgendamentoCadastroDto.builder()
            .valorTotal(valorTotal)
            .tempoDuracaoEmMinutos(tempoDuracaoEmMinutos)
            .dataHora(dataHora)
            .clienteId(1L)
            .barbeariaId(1L)
            .servicos(List.of(1L))
            .barbeiros(List.of(1L))
            .build();
    }

    private AgendamentoAtualizarDto montarAgendamentoAtualizarDto(Long agendamentoId, ZonedDateTime dataHora, BigDecimal valorTotal, Integer tempoDuracaoEmMinutos) {
        return AgendamentoAtualizarDto.builder()
                .id(agendamentoId)
                .dataHora(dataHora)
                .valorTotal(valorTotal)
                .tempoDuracaoEmMinutos(tempoDuracaoEmMinutos)
                .build();
    }
}
