package com.optimiza.clickbarber.controller;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaUtils;
import com.optimiza.clickbarber.service.BarbeariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    private final BarbeariaService barbeariaService;

    @Autowired
    public BarbeariaController(BarbeariaService barbeariaService) {
        this.barbeariaService = barbeariaService;
    }

    @GetMapping("/{email}")
    public Resposta<Object> findByEmail(@PathVariable String email) {
        var barbearia = barbeariaService.buscarPorEmail(email);
        return RespostaUtils.ok("Barbearia encontrada com sucesso!", barbearia);
    }

}
