package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByBarbeariaId(@Param("barbeariaId") Integer barbeariaId);

}
