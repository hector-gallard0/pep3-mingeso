package com.usach.msasignaturas.utils;

import com.usach.msasignaturas.dtos.AsignaturaDTO;
import com.usach.msasignaturas.entities.Asignatura;
import com.usach.msasignaturas.entities.Horario;

import java.util.List;

public class Mapper {
    public static AsignaturaDTO asignaturaToAsignaturaDTO(Asignatura asignatura, List<Integer> codigosPrerrequisitos) {
        return AsignaturaDTO.builder()
                .codigo(asignatura.getCodigo())
                .nivel(asignatura.getNivel())
                .codigoPlan(asignatura.getCodigoPlan())
                .nombre(asignatura.getNombre())
                .horarios(asignatura.getHorarios().stream().map(Horario::getHorario).toList())
                .codigosPrerrequisitos(codigosPrerrequisitos)
                .cupos(asignatura.getCupos())
                .inscritos(asignatura.getInscritos())
                .build();
    }
}
