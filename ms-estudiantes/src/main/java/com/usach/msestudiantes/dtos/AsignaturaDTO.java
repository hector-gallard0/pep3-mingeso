package com.usach.msestudiantes.dtos;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AsignaturaDTO {
    Integer codigo;
    String codigoPlan;
    Integer nivel;
    String nombre;
    Integer cupos;
    Integer inscritos;
    List<String> horarios;
    List<Integer> codigosPrerrequisitos;
}
