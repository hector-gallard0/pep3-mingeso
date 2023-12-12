package com.usach.msestudiantes.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "carrera")
@Setter
@Getter
public class Carrera {
    @Id
    @Column(name = "codigo")
    Integer codigo;

    @Column(name = "nombre")
    String nombre;
}
