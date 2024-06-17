package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.config.JwtUtil;
import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.Role;
import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.autenticacao.LoginRequestDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.cliente.ClienteDto;
import com.optimiza.clickbarber.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AutenticacaoServiceTest {

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private BarbeariaService barbeariaService;

    @Mock
    private BarbeiroService barbeiroService;

    @Mock
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Long usuarioId;
    private String email;
    private String senha;
    private UUID barbeariaIdExterno;
    private UUID clienteIdExterno;

    @BeforeEach
    void setup() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        usuarioId = 1L;
        email = "teste@mail.com";
        senha = "teste";
        barbeariaIdExterno = UUID.randomUUID();
        clienteIdExterno = UUID.randomUUID();
    }

    @Test
    void testLoginCliente() {
        var usuario = montarUsuario(Role.CLIENTE);
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuario);

        var cliente = montarClienteDto();
        when(clienteService.buscarPorUsuarioId(anyLong())).thenReturn(cliente);

        when(jwtUtil.gerarToken(anyString())).thenReturn("token_cliente");

        var loginRequest = montarLoginRequestDto();
        var resposta = autenticacaoService.login(loginRequest);

        assertNotNull(resposta);
        assertTrue(resposta.isSuccess());
        assertEquals(Constants.Success.LOGIN_REALIZADO_COM_SUCESSO, resposta.getMessage());
        assertEquals("token_cliente", resposta.getAccessToken());
        assertNotNull(resposta.getResult());
        assertInstanceOf(ClienteDto.class, resposta.getResult());
    }

    @Test
    void testLoginBarbearia() {
        var usuario = montarUsuario(Role.BARBEARIA);
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuario);

        var barbearia = montarBarbeariaDto();
        when(barbeariaService.buscarPorUsuarioId(anyLong())).thenReturn(barbearia);

        when(jwtUtil.gerarToken(anyString())).thenReturn("token_barbearia");

        var loginRequest = montarLoginRequestDto();
        var resposta = autenticacaoService.login(loginRequest);

        assertNotNull(resposta);
        assertTrue(resposta.isSuccess());
        assertEquals(Constants.Success.LOGIN_REALIZADO_COM_SUCESSO, resposta.getMessage());
        assertEquals("token_barbearia", resposta.getAccessToken());
        assertNotNull(resposta.getResult());
        assertInstanceOf(BarbeariaDto.class, resposta.getResult());
    }

    @Test
    void testLoginBarbeiro() {
        var usuario = montarUsuario(Role.BARBEIRO);
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuario);

        var barbeiro = montarBarbeiro();
        when(barbeiroService.buscarPorUsuarioId(anyLong())).thenReturn(barbeiro);

        when(jwtUtil.gerarToken(anyString())).thenReturn("token_barbeiro");

        var loginRequest = montarLoginRequestDto();
        var resposta = autenticacaoService.login(loginRequest);

        assertNotNull(resposta);
        assertTrue(resposta.isSuccess());
        assertEquals(Constants.Success.LOGIN_REALIZADO_COM_SUCESSO, resposta.getMessage());
        assertEquals("token_barbeiro", resposta.getAccessToken());
        assertNotNull(resposta.getResult());
        assertInstanceOf(Barbeiro.class, resposta.getResult());
    }

    @Test
    void testLoginNaoAutorizado_SenhaIncorreta() {
        var usuario = montarUsuario(Role.BARBEARIA);
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuario);

        var loginRequest = montarLoginRequestDto("teste2");
        var resposta = autenticacaoService.login(loginRequest);

        assertNotNull(resposta);
        assertFalse(resposta.isSuccess());
        assertEquals(Constants.Error.EMAIL_OU_SENHA_INCORRETA, resposta.getMessage());
        assertNull(resposta.getResult());
        assertNull(resposta.getAccessToken());
    }

    @Test
    void testLoginNaoAutorizado_EmailNaoEncontrado() {
        when(usuarioService.buscarPorEmail(anyString())).thenThrow(new ResourceNotFoundException(Constants.Entity.USUARIO, Constants.Attribute.EMAIL, email));

        var loginRequest = montarLoginRequestDto();
        assertThrows(ResourceNotFoundException.class, () -> autenticacaoService.login(loginRequest));
    }

    private Usuario montarUsuario(Role role) {
        String senhaCriptografada = bCryptPasswordEncoder.encode(senha);

        return Usuario.builder()
                .id(usuarioId)
                .email(email)
                .senha(senhaCriptografada)
                .role(role)
                .build();
    }

    private ClienteDto montarClienteDto() {
        return ClienteDto.builder()
                .idExterno(clienteIdExterno)
                .nome("Cliente Teste")
                .cpf("012345678910")
                .celular("988888888")
                .dataNascimento(LocalDate.of(2001, 1, 1))
                .build();
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .idExterno(barbeariaIdExterno)
                .nome("Barbearia Teste")
                .cnpj("01234567891012")
                .endereco("Rua Teste, 123")
                .telefone("988888888")
                .build();
    }

    private Barbeiro montarBarbeiro() {
        return Barbeiro.builder()
                .id(1L)
                .nome("Barbeiro Teste")
                .ativo(true)
                .admin(false)
                .cpf("012345678910")
                .build();
    }

    private LoginRequestDto montarLoginRequestDto() {
        return LoginRequestDto.builder()
                .email(email)
                .senha(senha)
                .build();
    }

    private LoginRequestDto montarLoginRequestDto(String senha) {
        return LoginRequestDto.builder().email(email).senha(senha).build();
    }

}
