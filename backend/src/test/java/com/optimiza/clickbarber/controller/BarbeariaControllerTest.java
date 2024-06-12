package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.service.BarbeariaService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BarbeariaController.class, useDefaultFilters = false)
class BarbeariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BarbeariaService barbeariaService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BarbeariaController(barbeariaService)).build();
    }

    @Test
    void buscarBarbeariaPeloNome() throws Exception {
        var barbearia1 = montarBarbeariaDto();
        var barbearia2 = montarBarbeariaDto(2L, "Barbearia Teste 2");
        var barbeariasDtoLista = List.of(barbearia1, barbearia2);
        when(barbeariaService.buscarPorNome(anyString())).thenReturn(barbeariasDtoLista);

        var nomePesquisar = "Barbearia";

        mockMvc.perform(MockMvcRequestBuilders.get("/barbearias")
                .param("nome", nomePesquisar)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(Constants.Success.BARBEARIAS_ENCONTRADAS_PELO_NOME + nomePesquisar))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[0].nome").value("Barbearia Teste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.[1].nome").value("Barbearia Teste 2"));
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .id(1L)
                .nome("Barbearia Teste")
                .telefone("988888888")
                .endereco("Rua Teste, 123")
                .cnpj("01234567891011")
                .build();
    }

    private BarbeariaDto montarBarbeariaDto(Long id, String nome) {
        return BarbeariaDto.builder()
                .id(id)
                .nome(nome)
                .telefone("988888888")
                .endereco("Rua Teste, 123")
                .cnpj("01234567891011")
                .build();
    }

}
