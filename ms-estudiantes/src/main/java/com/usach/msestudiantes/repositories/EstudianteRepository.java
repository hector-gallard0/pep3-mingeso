package com.usach.msestudiantes.repositories;

import com.usach.msestudiantes.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {

    @Query("SELECT e FROM Estudiante e WHERE e.rut = :rut")
    Optional<Estudiante> findByRut(@Param("rut") String rut);
}
