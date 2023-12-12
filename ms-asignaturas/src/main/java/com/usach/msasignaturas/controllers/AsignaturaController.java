package com.usach.msasignaturas.controllers;

import com.usach.msasignaturas.dtos.AsignaturaDTO;
import com.usach.msasignaturas.entities.Asignatura;
import com.usach.msasignaturas.requests.IngresarHorarioRequest;
import com.usach.msasignaturas.requests.IngresarHorariosRequest;
import com.usach.msasignaturas.services.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    AsignaturaService asignaturaService;

    @Autowired
    public AsignaturaController(AsignaturaService asignaturaService){
        this.asignaturaService = asignaturaService;
    }

    @GetMapping
    public List<AsignaturaDTO> obtenerAsignaturas() {
        return asignaturaService.obtenerAsignaturas();
    }

    @GetMapping("/{codigoCarrera}")
    public List<AsignaturaDTO> obtenerAsignaturasCarrera(@PathVariable Integer codigoCarrera) {
        return asignaturaService.obtenerAsignaturasCarrera(codigoCarrera);
    }

    @PostMapping("/horarios")
    public boolean ingresarHorarios(@RequestBody List<IngresarHorariosRequest> request){
        return asignaturaService.ingresarHorarios(request);
    }

    @PostMapping("/{codigoAsignatura}/horarios")
    public Asignatura ingresarHorario(@PathVariable Integer codigoAsignatura, @RequestBody IngresarHorarioRequest request){
        return asignaturaService.ingresarHorario(codigoAsignatura, request.getIdHorario());
    }
}
