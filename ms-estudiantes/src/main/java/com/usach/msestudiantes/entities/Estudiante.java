package com.usach.msestudiantes.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estudiante")
@Setter
@Getter
public class Estudiante {
    @Id
    @Column(name = "rut")
    String rut;

    @Column(name = "nombre")
    String nombre;

    @Column(name = "apellido")
    String apellido;

    @Column(name = "email")
    String email;

    @Column(name = "proceso")
    Boolean proceso;

    @ManyToOne
    @JoinColumn(name = "codigo_carrera")
    Carrera carrera;
}
