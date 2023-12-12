package com.usach.msasignaturas.repositories;

import com.usach.msasignaturas.entities.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {
    @Query("SELECT a " +
            "FROM Asignatura a " +
            "WHERE a.codigoCarrera = :codigoCarrera")
    List<Asignatura>findAllByCodigoCarrera(@Param("codigoCarrera") Integer codigoCarrera);
}
