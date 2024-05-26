package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Integer> {

    Optional<Barbearia> findByEmail(String email);

    @Query("SELECT b.senha FROM Barbearia b WHERE b.id = :id")
    String findPasswordById(@Param("id") Integer id);

}
