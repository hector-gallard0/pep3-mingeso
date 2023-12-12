package com.usach.msestudiantes.dtos;

import com.usach.msestudiantes.entities.Carrera;
import com.usach.msestudiantes.entities.EstudianteAsignatura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDTO {
    String rut;
    String nombre;
    String apellido;
    String email;
    Integer nivel;
    Boolean proceso;
    Carrera carrera;
    List<AsignaturaDTO> asignaturas;
    List<EstudianteAsignatura> asignaturasCursadas;
}
