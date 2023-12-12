package com.usach.msestudiantes.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AsignaturaRequest {
    Integer codigo;
    List<String> horarios;
}
