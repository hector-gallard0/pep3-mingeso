package com.usach.msestudiantes.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nota")
@Setter
@Getter
public class Nota {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "anio")
    Integer anio;

    @Column(name = "codigo_asignatura")
    Integer codigoAsignatura;

    @Column(name = "semestre")
    Integer semestre;

    @Column(name = "nota")
    Double nota;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante")
    Estudiante estudiante;
}
