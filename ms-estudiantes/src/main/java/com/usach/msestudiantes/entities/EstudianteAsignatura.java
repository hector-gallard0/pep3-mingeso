package com.usach.msestudiantes.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estudiante_asignatura")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteAsignatura {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "rut_estudiante")
    String rutEstudiante;

    @Column(name = "codigo_asignatura")
    Integer codigoAsignatura;

    @Column(name = "id_estado_estudiante_asignatura")
    Integer idEstado;

    @Column(name = "reprobaciones")
    Integer reprobaciones;
}

