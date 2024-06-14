package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioId(Long usuarioId);

}
