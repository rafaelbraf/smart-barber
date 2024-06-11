package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Long> {

    Optional<Barbearia> findByUsuarioId(UUID usuarioId);

}
