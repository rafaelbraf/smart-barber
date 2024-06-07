package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.exception.ResourceNotFoundException;
import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroAtualizarDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroCadastroDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroDto;
import com.optimiza.clickbarber.model.dto.barbeiro.BarbeiroMapper;
import com.optimiza.clickbarber.repository.BarbeiroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BarbeiroServiceTest {

    @InjectMocks
    private BarbeiroService barbeiroService;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private BarbeariaService barbeariaService;

    @Mock
    private BarbeiroMapper barbeiroMapper;

    @Test
    void testBuscarPorId_Encontrado() {
        var barbeiroEncontrado = montarBarbeiro();

        when(barbeiroRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(barbeiroEncontrado));

        var barbeiroResult = barbeiroService.buscarPorId(1);

        assertNotNull(barbeiroResult);
        assertEquals(1, barbeiroResult.getId());
        assertEquals("Barbeiro Teste", barbeiroResult.getNome());
        assertEquals(1, barbeiroResult.getBarbearia().getId());
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        when(barbeiroRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barbeiroService.buscarPorId(1));
    }

    @Test
    void testBuscarPorBarbeariaId_Encontrados() {
        var barbeiro1 = montarBarbeiro();
        var barbeiro2 = montarBarbeiro();
        barbeiro2.setId(2);
        var barbeirosEncontrados = List.of(barbeiro1, barbeiro2);

        when(barbeiroRepository.findByBarbeariaId(anyInt())).thenReturn(barbeirosEncontrados);

        var barbeirosResult = barbeiroService.buscarPorBarbeariaId(1);

        assertFalse(barbeirosResult.isEmpty());
        assertEquals(1, barbeirosResult.getFirst().getId());
        assertEquals(1, barbeirosResult.getFirst().getBarbearia().getId());
        assertEquals(2, barbeirosResult.getLast().getId());
        assertEquals(1, barbeirosResult.getLast().getBarbearia().getId());
    }

    @Test
    void testBuscarPorBarbeariaId_NaoEncontrados() {
        when(barbeiroRepository.findByBarbeariaId(anyInt())).thenReturn(Collections.emptyList());

        var barbeirosResult = barbeiroService.buscarPorBarbeariaId(1);

        assertTrue(barbeirosResult.isEmpty());
    }

    @Test
    void testBuscarPorUsuarioId_Encontrado() {
        var barbeiro = montarBarbeiro();

        when(barbeiroRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.of(barbeiro));

        var barbeiroResult = barbeiroService.buscarPorUsuarioId(UUID.randomUUID());

        assertNotNull(barbeiroResult);
        assertEquals(1, barbeiroResult.getId());
        assertEquals("Barbeiro Teste", barbeiroResult.getNome());
        assertEquals(1, barbeiroResult.getBarbearia().getId());
    }

    @Test
    void testBuscarPorUsuarioId_NaoEncontrados() {
        when(barbeiroRepository.findByUsuarioId(any(UUID.class))).thenReturn(Optional.empty());

        var usuarioId = UUID.randomUUID();
        assertThrows(ResourceNotFoundException.class, () -> barbeiroService.buscarPorUsuarioId(usuarioId));
    }

    @Test
    void testCadastrarBarbeiro() {
        when(barbeariaService.existePorId(anyInt())).thenReturn(true);

        var barbearia = montarBarbearia();
        when(barbeariaService.buscarPorId(anyInt())).thenReturn(barbearia);

        var barbeiro = montarBarbeiro();
        when(barbeiroMapper.toEntity(any(BarbeiroCadastroDto.class))).thenReturn(barbeiro);
        when(barbeiroRepository.save(any(Barbeiro.class))).thenReturn(barbeiro);

        var barbeiroDto = montarBarbeiroDto();
        when(barbeiroMapper.toDto(any(Barbeiro.class))).thenReturn(barbeiroDto);

        var barbeiroCadastroDto = montarBarbeiroCadastroDto();
        var barbeiroCadastradoResult = barbeiroService.cadastrar(barbeiroCadastroDto);

        assertNotNull(barbeiroCadastradoResult);
        assertEquals(1, barbeiroCadastradoResult.getId());
        assertEquals("Barbeiro Teste", barbeiroCadastradoResult.getNome());
        assertEquals(1, barbeiroCadastradoResult.getBarbearia().getId());
    }

    @Test
    void testAtualizarBarbeiro() {
        var barbeiro = montarBarbeiro();
        when(barbeiroRepository.findById(anyInt())).thenReturn(Optional.ofNullable(barbeiro));

        var barbeiroAtualizado = montarBarbeiro();
        barbeiroAtualizado.setNome("Barbeiro Atualizar");
        barbeiroAtualizado.setCelular("988888889");
        barbeiroAtualizado.setAdmin(true);
        when(barbeiroRepository.save(any(Barbeiro.class))).thenReturn(barbeiroAtualizado);

        var barbeiroAtualizarDto = montarBarbeiroAtualizarDto();
        var barbeiroAtualizadoResult = barbeiroService.atualizar(barbeiroAtualizarDto);

        assertNotNull(barbeiroAtualizadoResult);
        assertEquals(1, barbeiroAtualizadoResult.getId());
        assertEquals("Barbeiro Atualizar", barbeiroAtualizarDto.getNome());
        assertEquals("988888889", barbeiroAtualizarDto.getCelular());
    }

    @Test
    void testDeletarBarbeiroPorId() {
        barbeiroService.deletarPorId(1);
        verify(barbeiroRepository, times(1)).deleteById(anyInt());
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

    private BarbeiroDto montarBarbeiroDto() {
        return BarbeiroDto.builder()
                .id(1)
                .nome("Barbeiro Teste")
                .cpf("012345678910")
                .celular("988888888")
                .ativo(true)
                .admin(false)
                .barbearia(montarBarbeariaDto())
                .build();
    }

    private BarbeiroCadastroDto montarBarbeiroCadastroDto() {
        return BarbeiroCadastroDto.builder()
                .nome("Barbeiro Cadastrar")
                .cpf("012345678910")
                .admin(true)
                .ativo(true)
                .celular("988888888")
                .barbearia(montarBarbeariaDto())
                .build();
    }

    private BarbeiroAtualizarDto montarBarbeiroAtualizarDto() {
        return BarbeiroAtualizarDto.builder()
                .id(1)
                .nome("Barbeiro Atualizar")
                .celular("988888889")
                .admin(true)
                .ativo(true)
                .build();
    }

    private Barbearia montarBarbearia() {
        return Barbearia.builder()
                .id(1)
                .nome("Barbearia Teste")
                .cnpj("01234567891011")
                .endereco("Rua Teste, 123")
                .telefone("988888888")
                .build();
    }

    private BarbeariaDto montarBarbeariaDto() {
        return BarbeariaDto.builder()
                .id(1)
                .nome("Barbearia Teste")
                .cnpj("01234567891011")
                .endereco("Rua Teste, 123")
                .telefone("988888888")
                .build();
    }

}
