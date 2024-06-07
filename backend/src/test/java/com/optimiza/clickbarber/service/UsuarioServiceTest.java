package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Role;
import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioCadastrarDto;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioMapper;
import com.optimiza.clickbarber.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UUID usuarioId;

    @BeforeEach
    void setup() {
        usuarioId = UUID.randomUUID();
    }

    @Test
    void testBuscarUsuarioPorEmail_Encontrado() {
        var usuarioEncontrado = montarUsuario();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEncontrado));

        var usuarioEncontradoResult = usuarioService.buscarPorEmail("teste@mail.com");

        assertNotNull(usuarioEncontradoResult);
        assertEquals(usuarioId.toString(), usuarioEncontradoResult.getId().toString());
        assertEquals("teste@mail.com", usuarioEncontradoResult.getEmail());
        assertEquals("teste", usuarioEncontradoResult.getSenha());
    }

    @Test
    void testBuscarUsuarioPorEmail_NaoEncontrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorEmail("teste@mail.com"));
    }

    @Test
    void testCadastrarUsuario() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("senha_criptografada");

        var usuario = montarUsuario();
        when(usuarioMapper.toEntity(any(UsuarioCadastrarDto.class))).thenReturn(usuario);

        usuario.setSenha("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        var usuarioCadastrarDto = montarUsuarioCadastrarDto();
        var usuarioCadastradoResult = usuarioService.cadastrarUsuario(usuarioCadastrarDto);

        assertNotNull(usuarioCadastradoResult);
        assertEquals("teste@mail.com", usuarioCadastradoResult.getEmail());
        assertEquals("senha_criptografada", usuarioCadastradoResult.getSenha());
        assertEquals(Role.BARBEARIA, usuarioCadastradoResult.getRole());
    }

    private Usuario montarUsuario() {
        return Usuario.builder()
                .id(usuarioId)
                .email("teste@mail.com")
                .senha("teste")
                .role(Role.BARBEARIA)
                .build();
    }

    private UsuarioCadastrarDto montarUsuarioCadastrarDto() {
        return UsuarioCadastrarDto.builder()
                .email("teste@mail.com")
                .senha("teste")
                .role(Role.BARBEARIA)
                .build();
    }

}
