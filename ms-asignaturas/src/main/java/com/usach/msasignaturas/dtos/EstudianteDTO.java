package com.usach.msasignaturas.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstudianteDTO {
    String rut;
    String nombre;
    String apellido;
    String email;
    CarreraDTO carrera;
}
