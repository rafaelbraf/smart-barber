package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbeiro;
import com.optimiza.clickbarber.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Integer> {

    List<Barbeiro> findByBarbeariaId(Integer id);

    Optional<Barbeiro> findByUsuarioId(UUID usuarioId);

}
