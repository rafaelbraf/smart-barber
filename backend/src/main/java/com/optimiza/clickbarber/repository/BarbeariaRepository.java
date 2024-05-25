package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, Integer> {

    Optional<Barbearia> findByEmail(String email);

}
