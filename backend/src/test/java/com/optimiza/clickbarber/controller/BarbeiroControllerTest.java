package com.optimiza.clickbarber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.Role;
import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAtualizarDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroCadastroDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroDto;
import com.optimiza.clickbarber.service.BarbeiroService;
import com.optimiza.clickbarber.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BarbeiroController.class, useDefaultFilters = false)
class BarbeiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BarbeiroService barbeiroService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long barbeiroId;
    private UUID usuarioId;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BarbeiroController(barbeiroService)).build();

        barbeiroId = 1L;
        usuarioId = UUID.randomUUID();
    }

    @Test
    void testBuscarBarbeiroPorId() throws Exception {
        when(barbeiroService.buscarPorId(anyLong())).thenReturn(montarBarbeiro());

        mockMvc.perform(get("/barbeiros/" + barbeiroId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constants.Success.BARBEIRO_ENCONTRADO_COM_SUCESSO))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.cpf").value("01345678910"))
                .andExpect(jsonPath("$.result.usuario.id").value(usuarioId.toString()))
                .andExpect(jsonPath("$.result.usuario.role").value(Role.BARBEIRO.toString()))
                .andExpect(jsonPath("$.result.barbearia.id").value(1))
                .andExpect(jsonPath("$.result.barbearia.nome").value("Barbearia Teste"));
    }

    @Test
    void testBuscarBarbeirosPorBarbeariaId() throws Exception {
        var barbeiro1 = montarBarbeiro();
        var barbeiro2 = montarBarbeiro();
        barbeiro2.setId(2L);

        when(barbeiroService.buscarPorBarbeariaId(anyInt())).thenReturn(List.of(barbeiro1, barbeiro2));

        mockMvc.perform(get("/barbeiros/barbearia/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constants.Success.BARBEIROS_ENCONTRADOS_DA_BARBEARIA + 1))
                .andExpect(jsonPath("$.result.[0].barbearia.id").value(1))
                .andExpect(jsonPath("$.result.[1].barbearia.id").value(1));
    }

    @Test
    void testCadastrarBarbeiro() throws Exception {
        var barbeiroCadastrado = montarBarbeiroDto();
        when(barbeiroService.cadastrar(any(BarbeiroCadastroDto.class))).thenReturn(barbeiroCadastrado);

        var barbeiroCadastrar = montarBarbeiroCadastroDto();

        mockMvc.perform(post("/barbeiros")
                .content(objectMapper.writeValueAsString(barbeiroCadastrar))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(Constants.Success.BARBEIRO_CADASTRADO_COM_SUCESSO))
                .andExpect(jsonPath("$.result.nome").value("Barbeiro Teste"))
                .andExpect(jsonPath("$.result.barbearia.id").value(1));
    }

    @Test
    void testAtualizarBarbeiro() throws Exception {
        var barbeiro = montarBarbeiro();
        barbeiro.setAtivo(false);
        barbeiro.setNome("Barbeiro Atualizado");
        when(barbeiroService.atualizar(any(BarbeiroAtualizarDto.class))).thenReturn(barbeiro);

        var barbeiroAtualizarDto = BarbeiroAtualizarDto.builder()
                .id(1L)
                .nome("Barbeiro Atualizado")
                .ativo(false)
                .admin(false)
                .celular("988888888")
                .build();

        mockMvc.perform(put("/barbeiros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(barbeiroAtualizarDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constants.Success.BARBEIRO_ATUALIZADO_COM_SUCESSO))
                .andExpect(jsonPath("$.result.nome").value("Barbeiro Atualizado"))
                .andExpect(jsonPath("$.result.ativo").value(false));
    }

    @Test
    void testDeletarBarbeiro() throws Exception {
        doNothing().when(barbeiroService).deletarPorId(barbeiroId);

        mockMvc.perform(delete("/barbeiros/" + barbeiroId))
                .andExpect(status().isNoContent());
    }

    private Barbeiro montarBarbeiro() {
        return Barbeiro.builder()
            .id(barbeiroId)
            .cpf("01345678910")
            .nome("Barbeiro Teste")
            .admin(false)
            .celular("988888888")
            .ativo(true)
            .barbearia(montarBarbearia())
            .usuario(montarUsuario(usuarioId))
            .build();
    }

    private Barbearia montarBarbearia() {
        return Barbearia.builder()
                .id(1L)
                .nome("Barbearia Teste")
                .build();
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .id(1L)
                .cnpj("01234567891011")
                .telefone("988888888")
                .nome("Barbearia Teste")
                .endereco("Rua Teste, 1234")
                .build();
    }

    private Usuario montarUsuario(UUID id) {
        return Usuario.builder()
                .id(id)
                .role(Role.BARBEIRO)
                .build();
    }

    private BarbeiroCadastroDto montarBarbeiroCadastroDto() {
        return BarbeiroCadastroDto.builder()
                .barbearia(montarBarbeariaDto())
                .admin(false)
                .ativo(true)
                .celular("988888888")
                .nome("Barbeiro Teste")
                .usuario(montarUsuario(usuarioId))
                .cpf("013456789101")
                .build();
    }

    private BarbeiroDto montarBarbeiroDto() {
        return BarbeiroDto.builder()
                .id(1L)
                .nome("Barbeiro Teste")
                .ativo(true)
                .admin(false)
                .cpf("0134567891011")
                .celular("988888888")
                .barbearia(montarBarbeariaDto())
                .build();
    }

}
