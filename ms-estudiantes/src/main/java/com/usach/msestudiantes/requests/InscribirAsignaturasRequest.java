package com.usach.msestudiantes.requests;

import com.usach.msestudiantes.dtos.AsignaturaRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InscribirAsignaturasRequest {
    List<Integer> codigosAsignaturasRequest;
}
