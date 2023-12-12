package com.usach.msasignaturas.repositories;

import com.usach.msasignaturas.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    @Query("SELECT h FROM Horario h WHERE h.horario = :horario")
    Optional<Horario> findByHorario(@Param("horario") String horario);
}
