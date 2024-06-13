package com.optimiza.clickbarber.repository

import com.optimiza.clickbarber.model.Barbearia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BarbeariaRepository : JpaRepository<Barbearia, Long> {
    fun findByUsuarioId(usuarioId: UUID?): Optional<Barbearia>

    @Query("SELECT b FROM Barbearia b WHERE nome like %:nome%")
    fun findByNome(nome: String): List<Barbearia>
}