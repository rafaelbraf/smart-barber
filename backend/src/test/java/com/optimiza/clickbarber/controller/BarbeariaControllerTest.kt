package com.optimiza.clickbarber.controller

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto
import com.optimiza.clickbarber.service.BarbeariaService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@WebMvcTest(value = [BarbeariaController::class], useDefaultFilters = false)
internal class BarbeariaControllerTest {

    @Autowired
    private var mockMvc: MockMvc? = null

    @Mock
    private val barbeariaService: BarbeariaService? = null

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(BarbeariaController(barbeariaService!!)).build()
    }

    @Test
    fun buscarTodasAsBarbeariasSemLimite() {
        val barbearia1 = montarBarbeariaDto()
        val barbearia2 = montarBarbeariaDto(2, "Barbearia Teste 2")
        val barbeariasDtoLista = listOf(barbearia1, barbearia2)

        Mockito.`when`(barbeariaService!!.buscarTodos(any(Pageable::class.java))).thenReturn(barbeariasDtoLista)

        mockMvc!!.perform(get("/barbearias")
            .param("limit", null)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("${barbeariasDtoLista.size} ${Constants.Success.BARBEARIAS_ENCONTRADAS}"))
            .andExpect(jsonPath("$.result.[0].id").value(1))
            .andExpect(jsonPath("$.result.[0].nome").value("Barbearia Teste"))
            .andExpect(jsonPath("$.result.[1].id").value(2))
            .andExpect(jsonPath("$.result.[1].nome").value("Barbearia Teste 2"))
    }

    @Test
    fun buscarTodasAsBarbeariasComLimite() {
        val barbearia1 = montarBarbeariaDto()
        val barbeariasDtoLista = listOf(barbearia1)

        Mockito.`when`(barbeariaService!!.buscarTodos(any(Pageable::class.java))).thenReturn(barbeariasDtoLista)

        mockMvc!!.perform(get("/barbearias")
            .param("limit", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("${barbeariasDtoLista.size} ${Constants.Success.BARBEARIAS_ENCONTRADAS}"))
            .andExpect(jsonPath("$.result.[0].id").value(1))
            .andExpect(jsonPath("$.result.[0].nome").value("Barbearia Teste"))
    }

    @Test
    @Throws(Exception::class)
    fun buscarBarbeariaPeloNome() {
        val barbearia1 = montarBarbeariaDto()
        val barbearia2 = montarBarbeariaDto(2L, "Barbearia Teste 2")
        val barbeariasDtoLista = listOf(barbearia1, barbearia2)
        Mockito.`when`(barbeariaService!!.buscarPorNome(ArgumentMatchers.anyString())).thenReturn(barbeariasDtoLista)

        val nomePesquisar = "Barbearia"

        mockMvc!!.perform(get("/barbearias/nome")
            .param("nome", nomePesquisar)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("${Constants.Success.BARBEARIAS_ENCONTRADAS_PELO_NOME} $nomePesquisar"))
            .andExpect(jsonPath("$.result.[0].id").value(1))
            .andExpect(jsonPath("$.result.[0].nome").value("Barbearia Teste"))
            .andExpect(jsonPath("$.result.[1].id").value(2))
            .andExpect(jsonPath("$.result.[1].nome").value("Barbearia Teste 2"))
    }

    private fun montarBarbeariaDto(): BarbeariaDto {
        return BarbeariaDto.builder()
            .id(1L)
            .nome("Barbearia Teste")
            .telefone("988888888")
            .endereco("Rua Teste, 123")
            .cnpj("01234567891011")
            .build()
    }

    private fun montarBarbeariaDto(id: Long, nome: String): BarbeariaDto {
        return BarbeariaDto.builder()
            .id(id)
            .nome(nome)
            .telefone("988888888")
            .endereco("Rua Teste, 123")
            .cnpj("01234567891011")
            .build()
    }
}