package com.usach.msasignaturas.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarreraDTO {
    Integer codigo;
    String nombre;
}
