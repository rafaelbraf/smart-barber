package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Integer> {

    List<Barbeiro> findByBarbeariaId(Integer id);

}
