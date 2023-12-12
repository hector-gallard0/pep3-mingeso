package com.usach.msasignaturas.repositories;

import com.usach.msasignaturas.entities.Prerrequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerrequisitoRepository extends JpaRepository<Prerrequisito, Integer> {
    @Query("SELECT p.codigoPrerrequisito " +
            "FROM Prerrequisito p " +
            "WHERE p.codigoAsignatura = :codigoAsignatura ")
    List<Integer> findAllByCodigoAsignatura(@Param("codigoAsignatura") Integer codigoAsignatura);
}
