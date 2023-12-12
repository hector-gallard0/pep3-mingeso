package com.usach.msasignaturas.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngresarHorariosRequest {
    Integer codigoAsignatura;
    List<String> codigosHorarios;
}
