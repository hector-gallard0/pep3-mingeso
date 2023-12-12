package com.usach.msestudiantes.repositories;

import com.usach.msestudiantes.entities.EstudianteAsignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteAsignaturaRepository extends JpaRepository<EstudianteAsignatura, Integer> {
    @Query("SELECT d " +
            "FROM EstudianteAsignatura d " +
            "WHERE d.rutEstudiante = :rutEstudiante ")
    List<EstudianteAsignatura> findAllByRutEstudiante(@Param("rutEstudiante") String rutEstudiante);

}
