package com.optimiza.clickbarber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.service.BarbeariaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BarbeariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BarbeariaService barbeariaService;

    private BarbeariaController barbeariaController;

    @BeforeEach
    void setUp() {
        barbeariaController = new BarbeariaController(barbeariaService);
        mockMvc = MockMvcBuilders.standaloneSetup(barbeariaController).build();
    }

    @Test
    void testBuscarBarbeariaPorEmail_Encontrado() throws Exception {
        String email = "barbearia@mail.com";
        BarbeariaDto barbearia = BarbeariaDto.builder().email(email).nome("Barbearia Teste").build();

        when(barbeariaService.findByEmail(anyString())).thenReturn(barbearia);

        mockMvc.perform(get("/barbearias/" + email)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Barbearia encontrada com sucesso!")))
                .andExpect(jsonPath("$.result.email", is("barbearia@mail.com")))
                .andExpect(jsonPath("$.result.nome", is("Barbearia Teste")));
    }

    @Test
    void testBuscarBarbeariaPorEmail_NaoEncontrado() throws Exception {
        String email = "naoexiste@teste.com";

        Mockito.when(barbeariaService.findByEmail(email)).thenThrow(new NoSuchElementException("Não foi encontrada Barbearia com email " + email));

        mockMvc.perform(MockMvcRequestBuilders.get("/barbearias/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Não foi encontrada Barbearia com email " + email)))
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    void testCadastrarBarbearia() throws Exception {
        BarbeariaCadastroDto barbeariaCadastro = montarBarbeariaCadastroDto();
        BarbeariaDto barbeariaDto = montarBarbeariaDto();

        when(barbeariaService.cadastrar(any(BarbeariaCadastroDto.class))).thenReturn(barbeariaDto);

        mockMvc.perform(post("/barbearias/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(barbeariaCadastro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Barbearia cadastrada com sucesso!"))
                .andExpect(jsonPath("$.result").value(barbeariaDto));
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .id(1)
                .nome("Barbearia Teste")
                .endereco("Rua Teste, 123")
                .email("barbearia@teste.com")
                .telefone("88988888888")
                .build();
    }

    private BarbeariaCadastroDto montarBarbeariaCadastroDto() {
        return BarbeariaCadastroDto.builder()
                .nome("Barbearia Teste")
                .endereco("Rua Teste, 123")
                .email("barbearia@teste.com")
                .telefone("88988888888")
                .build();
    }

}
