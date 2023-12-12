package com.usach.msasignaturas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prerrequisito")
@NoArgsConstructor
@Getter
@Setter
public class Prerrequisito {
    @Id
    @Column(name = "id")
    Integer id;

    @Column(name = "codigo_asignatura")
    Integer codigoAsignatura;

    @Column(name = "codigo_prerrequisito")
    Integer codigoPrerrequisito;
}
