package com.optimiza.clickbarber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaAutenticacao;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.service.AutenticacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BarbeariaMapper barbeariaMapper;

    @MockBean
    private AutenticacaoService autenticacaoService;

    private LoginRequestDto loginRequest;
    private BarbeariaCadastroDto barbeariaCadastro;
    private BarbeariaDto barbearia;

    @BeforeEach
    void setUp() {
        AutenticacaoController autenticacaoController = new AutenticacaoController(autenticacaoService);
        mockMvc = MockMvcBuilders.standaloneSetup(autenticacaoController).build();

        loginRequest = LoginRequestDto.builder()
                .email("teste@mail.com")
                .senha("teste")
                .build();

        barbeariaCadastro = BarbeariaCadastroDto.builder()
                .cnpj("12345678901234")
                .nome("Barbearia Teste")
                .endereco("Rua Teste, 123")
                .email("barbearia@teste.com")
                .senha("teste")
                .telefone("88988888888")
                .build();
    }

    @Test
    void testLoginBarbearia_Sucesso() throws Exception {
        BarbeariaDto barbeariaDto = montarBarbeariaDto();
        RespostaAutenticacao<Object> respostaAutenticacao = montarRespostaAutenticacaoComObject(barbeariaDto);

        when(autenticacaoService.loginBarbearia(any(LoginRequestDto.class))).thenReturn(respostaAutenticacao);

        mockMvc.perform(post("/auth/barbearias/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.result").value(barbeariaDto));
    }

    @Test
    void testLoginBarbearia_Falha() throws Exception {
        RespostaAutenticacao<Object> respostaAutenticacao = montarRespostaAutenticacaoFalha();

        when(autenticacaoService.loginBarbearia(any(LoginRequestDto.class))).thenReturn(respostaAutenticacao);

        mockMvc.perform(post("/auth/barbearias/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("Email ou senha incorreta"))
                .andExpect(jsonPath("$.accessToken").doesNotExist())
                .andExpect(jsonPath("$.result").doesNotExist());
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

    private RespostaAutenticacao<Object> montarRespostaAutenticacaoComObject(Object result) {
        return RespostaAutenticacao.builder()
                .accessToken("token")
                .success(true)
                .statusCode(200)
                .message("Login realizado com sucesso!")
                .result(result)
                .build();
    }

    private RespostaAutenticacao<Object> montarRespostaAutenticacaoFalha() {
        return RespostaAutenticacao.builder()
                .success(false)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Email ou senha incorreta")
                .accessToken(null)
                .build();
    }

    private Resposta<BarbeariaDto> montarResposta(int statusCode, boolean success, String message, BarbeariaDto barbearia) {
        return Resposta.<BarbeariaDto>builder()
                .statusCode(statusCode)
                .success(success)
                .message(message)
                .result(barbearia)
                .build();
    }

}
