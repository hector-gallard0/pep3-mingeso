package com.usach.msestudiantes.controllers;

import com.usach.msestudiantes.dtos.AsignaturaDTO;
import com.usach.msestudiantes.dtos.AsignaturaRequest;
import com.usach.msestudiantes.dtos.EstudianteDTO;
import com.usach.msestudiantes.entities.Carrera;
import com.usach.msestudiantes.entities.Estudiante;
import com.usach.msestudiantes.requests.InscribirAsignaturasRequest;
import com.usach.msestudiantes.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EstudianteController {
    EstudianteService estudianteService;

    @Autowired
    EstudianteController(EstudianteService estudianteService){
        this.estudianteService = estudianteService;
    }

    @GetMapping("/estudiantes/{rut}")
    public EstudianteDTO obtenerEstudiante(@PathVariable String rut) {
        return estudianteService.obtenerEstudiante(rut);
    }

    @GetMapping("/carreras")
    public List<Carrera> obtenerCarreras() {
        return estudianteService.obtenerCarreras();
    }

    @PostMapping("/estudiantes/{rut}/asignaturas")
    public List<AsignaturaDTO> inscribirAsignaturas(@PathVariable String rut, @RequestBody List<Integer> codigosAsignaturasRequest){
        System.out.println(rut);
        System.out.println(codigosAsignaturasRequest);
        return estudianteService.inscribirAsignaturas(rut, codigosAsignaturasRequest);
    }
}
