package com.usach.msasignaturas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "asignatura")
@NoArgsConstructor
@Getter
@Setter
public class Asignatura {
    @Id
    @Column(name = "codigo")
    Integer codigo;

    @Column(name = "codigo_carrera")
    String codigoCarrera;

    @Column(name = "codigo_plan")
    String codigoPlan;

    @Column(name = "nivel")
    Integer nivel;

    @Column(name = "nombre")
    String nombre;

    @Column(name = "cupos")
    Integer cupos;

    @Column(name = "inscritos")
    Integer inscritos;

    @ManyToMany
    @JoinTable(
            name = "asignatura_horario",
            joinColumns = @JoinColumn(name = "codigo_asignatura"),
            inverseJoinColumns = @JoinColumn(name = "id_horario"))
    List<Horario> horarios;
}
