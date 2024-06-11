package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.repository.BarbeariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BarbeariaServiceTest {

    @InjectMocks
    private BarbeariaService barbeariaService;

    @Mock
    private BarbeariaRepository barbeariaRepository;

    @Mock
    private BarbeariaMapper barbeariaMapper;

    private UUID usuarioId;

    @BeforeEach
    void setup() {
        usuarioId = UUID.randomUUID();
    }

    @Test
    void testVerificaSeBarbeariaExiste() {
        when(barbeariaRepository.existsById(anyLong())).thenReturn(true);
        var existeBarbearia = barbeariaService.existePorId(1L);
        assertTrue(existeBarbearia);
    }

    @Test
    void testBuscarBarbeariaPorId_Encontrado() {
        var barbearia = montarBarbearia();
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.of(barbearia));

        var barbeariaEncontrada = barbeariaService.buscarPorId(1L);
        assertNotNull(barbeariaEncontrada);
        assertEquals(1, barbeariaEncontrada.getId());
        assertEquals("Barbearia Teste", barbeariaEncontrada.getNome());
    }

    @Test
    void testBuscarBarbeariaPorId_NaoEncontrado() {
        when(barbeariaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barbeariaService.buscarPorId(1L));
    }

    @Test
    void testBuscarBarbeariaPorUsuarioId_Encontrado() {
        var barbearia = montarBarbearia();
        when(barbeariaRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.of(barbearia));

        var barbeariaDto = montarBarbeariaDto();
        when(barbeariaMapper.toDto(any(Barbearia.class))).thenReturn(barbeariaDto);

        var barbeariaEncontrada = barbeariaService.buscarPorUsuarioId(usuarioId);
        assertNotNull(barbeariaEncontrada);
        assertEquals(1, barbeariaEncontrada.getId());
        assertEquals("Barbearia Teste", barbeariaEncontrada.getNome());
    }

    @Test
    void testBuscarBarbeariaPorUsuarioId_NaoEncontrado() {
        when(barbeariaRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barbeariaService.buscarPorUsuarioId(usuarioId));
    }

    @Test
    void testCadastrarBarbearia() {
        var barbearia = montarBarbearia();
        when(barbeariaMapper.toEntity(any(BarbeariaCadastroDto.class))).thenReturn(barbearia);
        when(barbeariaRepository.save(any(Barbearia.class))).thenReturn(barbearia);

        var barbeariaDto = montarBarbeariaDto();
        when(barbeariaMapper.toDto(any(Barbearia.class))).thenReturn(barbeariaDto);

        var barbeariaCadastroDto = montarBarbeariaCadastroDto();
        var barbeariaCadastradaResult = barbeariaService.cadastrar(barbeariaCadastroDto);
        assertNotNull(barbeariaCadastradaResult);
        assertEquals(1, barbeariaCadastradaResult.getId());
        assertEquals("Barbearia Teste", barbeariaCadastradaResult.getNome());
    }

    private Barbearia montarBarbearia() {
        return Barbearia.builder()
                .id(1L)
                .nome("Barbearia Teste")
                .cnpj("01234567891011")
                .telefone("988888888")
                .endereco("Rua Teste, 123")
                .build();
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

    private BarbeariaCadastroDto montarBarbeariaCadastroDto() {
        return BarbeariaCadastroDto.builder()
                .nome("Barbearia Teste")
                .cnpj("01234567891011")
                .endereco("Rua Teste, 123")
                .telefone("988888888")
                .build();
    }

}
