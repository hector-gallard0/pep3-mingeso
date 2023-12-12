package com.usach.msestudiantes.utils;

import com.usach.msestudiantes.dtos.AsignaturaDTO;
import com.usach.msestudiantes.dtos.EstudianteDTO;
import com.usach.msestudiantes.entities.Estudiante;
import com.usach.msestudiantes.entities.EstudianteAsignatura;

import java.util.List;

public class Mapper {
    public static EstudianteDTO asignaturaToAsignaturaDTO(Estudiante estudiante,
                                                          List<AsignaturaDTO> asignaturas,
                                                          List<EstudianteAsignatura> asignaturasCursadas,
                                                          Integer nivel) {
        return EstudianteDTO.builder()
                .rut(estudiante.getRut())
                .nombre(estudiante.getNombre())
                .apellido(estudiante.getApellido())
                .email(estudiante.getEmail())
                .carrera(estudiante.getCarrera())
                .nivel(nivel)
                .proceso(estudiante.getProceso())
                .asignaturas(asignaturas)
                .asignaturasCursadas(asignaturasCursadas)
                .build();
    }
}
