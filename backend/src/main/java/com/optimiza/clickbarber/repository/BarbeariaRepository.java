package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Long> {

    Optional<Barbearia> findByUsuarioId(Long usuarioId);

    @Query("SELECT b FROM Barbearia b WHERE b.nome LIKE %:nome%")
    List<Barbearia> findByNome(String nome);
}