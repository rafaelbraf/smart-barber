package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbearia;
import com.optimiza.clickbarber.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Integer> {

    Optional<Barbearia> findByUsuarioId(UUID usuarioId);

}
