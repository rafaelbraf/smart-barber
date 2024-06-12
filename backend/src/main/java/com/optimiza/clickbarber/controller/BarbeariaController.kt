package com.optimiza.clickbarber.controller

import com.optimiza.clickbarber.model.Resposta
import com.optimiza.clickbarber.model.RespostaUtils
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto
import com.optimiza.clickbarber.service.BarbeariaService
import com.optimiza.clickbarber.utils.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/barbearias")
class BarbeariaController @Autowired constructor(
    private val barbeariaService: BarbeariaService
) {

    @GetMapping
    fun buscarPorNome(@RequestParam(name = "nome") nome: String): Resposta<List<BarbeariaDto>> {
        val barbearias = barbeariaService.buscarPorNome(nome)
        val mensagem = "${Constants.Success.BARBEARIAS_ENCONTRADAS_PELO_NOME} $nome"
        return RespostaUtils.ok(mensagem, barbearias)
    }

}