package com.optimiza.clickbarber.repository;

import com.optimiza.clickbarber.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    List<Agendamento> findByBarbeariaId(Integer barbeariaId);

    List<Agendamento> findByDataHoraAndBarbeariaId(ZonedDateTime dataHora, Integer barbeariaId);

}
