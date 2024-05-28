package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Usuario;
import com.optimiza.clickbarber.model.dto.usuario.UsuarioDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u.senha FROM Usuario u WHERE u.id = :id")
    String findSenhaPorId(@Param("id") UUID id);

}
