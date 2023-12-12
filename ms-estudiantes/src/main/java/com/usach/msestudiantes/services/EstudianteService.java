package com.usach.msestudiantes.services;

import com.usach.msestudiantes.clients.AsignaturaFeignClient;
import com.usach.msestudiantes.dtos.AsignaturaDTO;
import com.usach.msestudiantes.dtos.EstudianteDTO;
import com.usach.msestudiantes.dtos.HorarioDTO;
import com.usach.msestudiantes.dtos.AsignaturaRequest;
import com.usach.msestudiantes.entities.Carrera;
import com.usach.msestudiantes.entities.Estudiante;
import com.usach.msestudiantes.entities.EstudianteAsignatura;
import com.usach.msestudiantes.repositories.CarreraRepository;
import com.usach.msestudiantes.repositories.EstudianteAsignaturaRepository;
import com.usach.msestudiantes.repositories.EstudianteRepository;
import com.usach.msestudiantes.repositories.NotaRepository;
import com.usach.msestudiantes.utils.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstudianteService {
    NotaRepository notaRepository;
    EstudianteRepository estudianteRepository;
    CarreraRepository carreraRepository;
    EstudianteAsignaturaRepository estudianteAsignaturaRepository;
    AsignaturaFeignClient asignaturaFeignClient;

    @Autowired
    EstudianteService(NotaRepository notaRepository,
                      EstudianteRepository estudianteRepository,
                      CarreraRepository carreraRepository,
                      EstudianteAsignaturaRepository estudianteAsignaturaRepository,
                      AsignaturaFeignClient asignaturaFeignClient){
        this.notaRepository = notaRepository;
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.estudianteAsignaturaRepository = estudianteAsignaturaRepository;
        this.asignaturaFeignClient = asignaturaFeignClient;
    }

    @Transactional
    public List<AsignaturaDTO> inscribirAsignaturas(String rut, List<Integer> codigosAsignaturasRequest) {
        if(codigosAsignaturasRequest == null || codigosAsignaturasRequest.size() < 3) throw new RuntimeException("El estudiante debe inscribir al menos 3 asignaturas de su plan de estudios.");

        Optional<Estudiante> estudianteOPT = estudianteRepository.findByRut(rut);
        if(estudianteOPT.isEmpty()) throw new RuntimeException("El estudiante de rut " + rut + " no existe.");

        verificarAsignaturasRepetidas(codigosAsignaturasRequest);

        Estudiante estudiante = estudianteOPT.get();

        if(!estudiante.getProceso()) throw new RuntimeException("El estudiante ya realizó el proceso de inscripción de asignaturas.");

        List<AsignaturaDTO> asignaturas = asignaturaFeignClient.obtenerPlanEstudios(estudiante.getCarrera().getCodigo());
        List<AsignaturaDTO> asignaturasAInscribir = obtenerAsignaturasAInscribir(asignaturas, codigosAsignaturasRequest);
        List<EstudianteAsignatura> estudianteAsignaturas = estudianteAsignaturaRepository.findAllByRutEstudiante(rut);
        List<AsignaturaDTO> asignaturasCursadas = obtenerAsignaturasCursadas(asignaturas, estudianteAsignaturas);
        List<AsignaturaDTO> asignaturasNivel = obtenerAsignaturasNivel(asignaturasCursadas, estudianteAsignaturas);

        //Verifica que las asignaturas correspondan al plan de estudios.
        if(codigosAsignaturasRequest.size() > asignaturasNivel.size()) throw new RuntimeException("El número máximo de asignaturas que puede inscribir el o la estudiante corresponde al número de asignaturas de su nivel.");

        verificarRequisitos(asignaturasAInscribir, asignaturasCursadas, estudianteAsignaturas);
        verificarTopeHorarios(asignaturasAInscribir);

        List<EstudianteAsignatura> asignaturasInscritas = new ArrayList<>();
        for(AsignaturaDTO asignaturaAInscribir : asignaturasAInscribir) {
            Optional<EstudianteAsignatura> estudianteAsignaturasAInscribir = estudianteAsignaturas.stream().filter(ea -> ea.getCodigoAsignatura().equals(asignaturaAInscribir.getCodigo())).findFirst();
            EstudianteAsignatura asignaturaInscrita;
            asignaturaInscrita = estudianteAsignaturasAInscribir.orElseGet(() -> EstudianteAsignatura.builder()
                    .codigoAsignatura(asignaturaAInscribir.getCodigo())
                    .rutEstudiante(rut)
                    .idEstado(1)
                    .reprobaciones(0)
                    .build());
            asignaturasInscritas.add(asignaturaInscrita);
        }
        estudianteAsignaturaRepository.saveAll(asignaturasInscritas);
        estudiante.setProceso(false);
        return asignaturas;
    }

    private void verificarAsignaturasRepetidas(List<Integer> codigosAsignaturasRequest) {
        List<Integer> codigosAsignaturasRequest2 = codigosAsignaturasRequest.stream().distinct().toList();
        if(codigosAsignaturasRequest.size() != codigosAsignaturasRequest2.size()) throw new RuntimeException("Existen asignaturas repetidas en la consulta.");
    }

    private void verificarTopeHorarios(List<AsignaturaDTO> asignaturasInscripcion) {
        for(int i = 0; i < asignaturasInscripcion.size(); i++) {
            AsignaturaDTO asignaturaInscripcion1 = asignaturasInscripcion.get(i);
            for(int j = i + 1; j < asignaturasInscripcion.size(); j++) {
                AsignaturaDTO asignaturaInscripcion2 = asignaturasInscripcion.get(j);
                if(asignaturaInscripcion1.getHorarios().stream().anyMatch(asignaturaInscripcion2.getHorarios()::contains)){
                    throw new RuntimeException("Existe tope de horarios entre las asignaturas " + asignaturaInscripcion1.getNombre() + " y " + asignaturaInscripcion2.getNombre() + ".");
                }
            }
        }
    }

    private void verificarRequisitos(List<AsignaturaDTO> asignaturasAInscribir, List<AsignaturaDTO> asignaturasCursadas, List<EstudianteAsignatura> estudiantesAsignaturas) {
        for(AsignaturaDTO asignaturaAInscribir : asignaturasAInscribir) {
            Optional<EstudianteAsignatura> estudianteAsignaturaAInscribirOPT = estudiantesAsignaturas.stream().filter(ea -> ea.getCodigoAsignatura().equals(asignaturaAInscribir.getCodigo())).findFirst();
            if(estudianteAsignaturaAInscribirOPT.isPresent()) {
                EstudianteAsignatura estudianteAsignaturaAInscribir = estudianteAsignaturaAInscribirOPT.get();
                if(estudianteAsignaturaAInscribir.getIdEstado() == 1) throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " ya fue inscrita.");
                if(estudianteAsignaturaAInscribir.getIdEstado() == 3) throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " ya fue aprobada.");
                if(estudianteAsignaturaAInscribir.getIdEstado() == 5) throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " está en proceso de solicitud.");
                // valida 3 reprobaciones o más a asignaturas de primer nivel
                if(asignaturaAInscribir.getNivel() == 1 && estudianteAsignaturaAInscribir.getReprobaciones() > 2) {
                    throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " es de nivel 1 y ha sido reprobada 3 o más veces, por lo cual usted está en causal de eliminación, contáctese con secretaría académica.");
                }
                // valida 2 reprobaciones o más a asignaturas que no sean de primer nivel.
                if(asignaturaAInscribir.getNivel() != 1 && estudianteAsignaturaAInscribir.getReprobaciones() > 1) {
                    throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " ha sido reprobada 2 o más veces, por lo cual usted está en causal de eliminación, contáctese con secretaría académica.");
                }


                for(Integer codigoPrerrequisito : asignaturaAInscribir.getCodigosPrerrequisitos()) {
                    AsignaturaDTO asignatura = asignaturasCursadas.stream().filter(a -> a.getCodigo().equals(codigoPrerrequisito)).findFirst().get();
                    EstudianteAsignatura estudianteAsignatura = estudiantesAsignaturas.stream().filter(ea -> ea.getCodigoAsignatura().equals(codigoPrerrequisito)).findFirst().get();
                    //si no tiene aprobado el prerrequisito
                    if(estudianteAsignatura.getIdEstado() != 3) {
                        throw new RuntimeException("El estudiante no puede inscribir la asignatura " +  asignatura.getNombre() + " porque no cumple con el prerrequisito " + codigoPrerrequisito);
                    }
                }
            }

            if(asignaturaAInscribir.getInscritos() >= asignaturaAInscribir.getCupos()){
                throw new RuntimeException("La asignatura " + asignaturaAInscribir.getNombre() + " no posee cupos disponibles ("
                        + asignaturaAInscribir.getInscritos() + "/" + asignaturaAInscribir.getCupos() + ").");
            }
        }
    }

    public List<AsignaturaDTO> obtenerAsignaturasCursadas(List<AsignaturaDTO> asignaturas, List<EstudianteAsignatura> estudianteAsignaturas) {
        List<Integer> codigosAsignaturasCursadas = estudianteAsignaturas.stream().map(EstudianteAsignatura::getCodigoAsignatura).toList();
        return asignaturas.stream()
                .filter(asignatura -> codigosAsignaturasCursadas.contains(asignatura.getCodigo()))
                .toList();
    }

    public Integer obtenerNivel(List<AsignaturaDTO> asignaturasCursadas, List<EstudianteAsignatura> estudianteAsignaturas){
        List<EstudianteAsignatura> asignaturasReprobadas = estudianteAsignaturas.stream().filter(ea -> ea.getIdEstado() == 2).toList();

        //Nunca será 20
        Integer nivelMinimoReprobado = 20;
        for(EstudianteAsignatura asignaturaReprobada : asignaturasReprobadas) {
            AsignaturaDTO asignatura = asignaturasCursadas.stream().filter(ac -> ac.getCodigo().equals(asignaturaReprobada.getCodigoAsignatura())).findFirst().get();
            if(asignatura.getNivel() < nivelMinimoReprobado) {
                nivelMinimoReprobado = asignatura.getNivel();
            }
        }

        return nivelMinimoReprobado;
    }

    public List<AsignaturaDTO> obtenerAsignaturasNivel(List<AsignaturaDTO> asignaturasCursadas, List<EstudianteAsignatura> estudianteAsignaturas){
        Integer nivelMinimoReprobado = obtenerNivel(asignaturasCursadas, estudianteAsignaturas);
        return asignaturasCursadas.stream().filter(asignatura -> asignatura.getNivel().equals(nivelMinimoReprobado)).toList();
    }

    public List<AsignaturaDTO> obtenerAsignaturasAInscribir(List<AsignaturaDTO> asignaturas, List<Integer> codigosAsignaturasRequest){
        List<AsignaturaDTO> asignaturasAInscribir = new ArrayList<>();
        for(Integer codigoAsignaturaRequest : codigosAsignaturasRequest) {
            Optional<AsignaturaDTO> asignaturaAInscribirOPT = asignaturas.stream().filter(asignatura -> asignatura.getCodigo().equals(codigoAsignaturaRequest)).findFirst();
            if(asignaturaAInscribirOPT.isEmpty()) throw new RuntimeException("Una de las asignaturas que desea inscribir no corresponde a su plan de estudios.");
            AsignaturaDTO asignaturaAInscribir = asignaturaAInscribirOPT.get();
            asignaturasAInscribir.add(asignaturaAInscribir);
        }
        return asignaturasAInscribir;
    }

    public List<Carrera> obtenerCarreras() {
        return carreraRepository.findAll();
    }

    public EstudianteDTO obtenerEstudiante(String rut) {
        Optional<Estudiante> estudianteOPT = estudianteRepository.findByRut(rut);
        if(estudianteOPT.isEmpty()) throw new RuntimeException("Estudiante no existe.");
        Estudiante estudiante = estudianteOPT.get();
        List<AsignaturaDTO> asignaturas = asignaturaFeignClient.obtenerPlanEstudios(estudiante.getCarrera().getCodigo());
        List<EstudianteAsignatura> estudianteAsignaturas = estudianteAsignaturaRepository.findAllByRutEstudiante(rut);
        List<AsignaturaDTO> asignaturasCursadas = obtenerAsignaturasCursadas(asignaturas, estudianteAsignaturas);
        Integer nivel = obtenerNivel(asignaturasCursadas, estudianteAsignaturas);

        return Mapper.asignaturaToAsignaturaDTO(estudiante, asignaturas, estudianteAsignaturas, nivel);
    }
}
