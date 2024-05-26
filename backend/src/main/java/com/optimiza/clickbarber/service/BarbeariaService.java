package com.optimiza.clickbarber.service;

import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaCadastroDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaDto;
import com.optimiza.clickbarber.model.dto.barbearia.BarbeariaMapper;
import com.optimiza.clickbarber.repository.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;
    private final BarbeariaMapper barbeariaMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public BarbeariaService(BarbeariaRepository barbeariaRepository, BarbeariaMapper barbeariaMapper) {
        this.barbeariaRepository = barbeariaRepository;
        this.barbeariaMapper = barbeariaMapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public BarbeariaDto buscarPorEmail(String email) {
        var barbearia = barbeariaRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Não foi encontrada Barbearia com email " + email));

        return barbeariaMapper.toDto(barbearia);
    }

    public String buscarSenha(Integer id) {
        return barbeariaRepository.findPasswordById(id);
    }

    public BarbeariaDto cadastrar(BarbeariaCadastroDto barbeariaCadastro) {
        var senhaCriptografada = bCryptPasswordEncoder.encode(barbeariaCadastro.getSenha());
        barbeariaCadastro.setSenha(senhaCriptografada);

        var barbearia = barbeariaMapper.toBarbearia(barbeariaCadastro);
        var barbeariaCadastrada = barbeariaRepository.save(barbearia);

        return barbeariaMapper.toDto(barbeariaCadastrada);
    }
}
