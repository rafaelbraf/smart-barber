package com.optimiza.clickbarber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.RespostaLogin;
import com.optimiza.clickbarber.model.Role;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioCadastrarDto;
import com.optimiza.clickbarber.service.AutenticacaoService;
import com.optimiza.clickbarber.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AutenticacaoController.class, useDefaultFilters = false)
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AutenticacaoService autenticacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AutenticacaoController(autenticacaoService)).build();
    }

    @Test
    void testLogin() throws Exception {
        var token = "token_teste";
        var barbearia = montarBarbeariaDto();
        var respostaLogin = RespostaLogin.authorized(barbearia, token);
        when(autenticacaoService.login(ArgumentMatchers.any(LoginRequestDto.class))).thenReturn(respostaLogin);

        var loginRequest = montarLoginRequestDto();
        var loginRequestJsonString = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                .content(loginRequestJsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constants.Success.LOGIN_REALIZADO_COM_SUCESSO))
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.nome").value("Barbearia Teste"));
    }

    @Test
    void testCadastrarUsuario() throws Exception {
        var barbeariaDto = montarBarbeariaDto();
        when(autenticacaoService.cadastrarUsuario(any(UsuarioCadastrarDto.class))).thenReturn(barbeariaDto);

        var barbearia = montarBarbearia();
        var usuarioParaCadastro = montarUsuarioBarbearia(barbearia);
        var usuarioParaCadastroJsonString = objectMapper.writeValueAsString(usuarioParaCadastro);

        mockMvc.perform(post("/auth/cadastrar")
                .content(usuarioParaCadastroJsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(Constants.Success.USUARIO_CADASTRADO_COM_SUCESSO))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.id").value(1));
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

    private Barbearia montarBarbearia() {
        return Barbearia.builder()
                .id(1L)
                .nome("Barbearia Teste")
                .cnpj("0123456789101112")
                .endereco("Rua Teste, 12")
                .build();
    }

    private LoginRequestDto montarLoginRequestDto() {
        return LoginRequestDto.builder()
                .email("teste@mail.com")
                .senha("teste")
                .build();
    }

    private UsuarioCadastrarDto montarUsuarioBarbearia(Barbearia barbearia) {
        return UsuarioCadastrarDto.builder()
                .email("teste@mail.com")
                .senha("teste")
                .role(Role.BARBEARIA)
                .data(barbearia)
                .build();
    }

}
