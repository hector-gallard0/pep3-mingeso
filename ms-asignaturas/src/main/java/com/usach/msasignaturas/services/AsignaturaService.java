package com.usach.msasignaturas.services;

import com.usach.msasignaturas.dtos.AsignaturaDTO;
import com.usach.msasignaturas.entities.Asignatura;
import com.usach.msasignaturas.entities.Horario;
import com.usach.msasignaturas.exceptions.ApiErrorException;
import com.usach.msasignaturas.repositories.AsignaturaRepository;
import com.usach.msasignaturas.repositories.HorarioRepository;
import com.usach.msasignaturas.repositories.PrerrequisitoRepository;
import com.usach.msasignaturas.requests.IngresarHorariosRequest;
import com.usach.msasignaturas.utils.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaService {

    AsignaturaRepository asignaturaRepository;
    HorarioRepository horarioRepository;
    PrerrequisitoRepository prerrequisitoRepository;

    @Autowired
    public AsignaturaService(AsignaturaRepository asignaturaRepository,
                             HorarioRepository horarioRepository,
                             PrerrequisitoRepository prerrequisitoRepository){
        this.asignaturaRepository = asignaturaRepository;
        this.horarioRepository = horarioRepository;
        this.prerrequisitoRepository = prerrequisitoRepository;
    }

    public List<AsignaturaDTO> obtenerAsignaturas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        return obtenerAsignaturasDTOConPrerrequisitos(asignaturas);
    }

    @Transactional
    public Asignatura ingresarHorario(Integer codigoAsignatura, Integer idHorario){
        Optional<Horario> horario = horarioRepository.findById(idHorario);
        if(horario.isEmpty()) throw new ApiErrorException("El horario no existe.");

        Optional<Asignatura> asignatura = asignaturaRepository.findById(codigoAsignatura);
        if(asignatura.isEmpty()) throw new ApiErrorException("La asignatura no existe.");

        Asignatura asignaturaEntity = asignatura.get();
        asignaturaEntity.getHorarios().add(horario.get());

        return asignaturaRepository.save(asignaturaEntity);
    }

    @Transactional
    public boolean ingresarHorarios(List<IngresarHorariosRequest> request) {
        List<Asignatura> asignaturasActualizadas = new ArrayList<>();
        for(IngresarHorariosRequest ingresarHorariosRequest : request) {
            Optional<Asignatura> asignatura = asignaturaRepository.findById(ingresarHorariosRequest.getCodigoAsignatura());
            if(asignatura.isEmpty()) throw new ApiErrorException("La asignatura no existe.");

            List<Horario> horariosEntities = new ArrayList<>();
            for(String codigoHorario : ingresarHorariosRequest.getCodigosHorarios()) {
                Optional<Horario> horario = horarioRepository.findByHorario(codigoHorario);
                if(horario.isEmpty()) throw new ApiErrorException("El horario no existe.");
                horariosEntities.add(horario.get());
            }

            Asignatura asignaturaEntity = asignatura.get();
            asignaturaEntity.setHorarios(horariosEntities);
            asignaturasActualizadas.add(asignaturaEntity);
        }

        return true;
    }

    @Transactional
    public List<AsignaturaDTO> obtenerAsignaturasCarrera(Integer codigoCarrera) {
        List<Asignatura> asignaturasCarrera = asignaturaRepository.findAllByCodigoCarrera(codigoCarrera);
        return obtenerAsignaturasDTOConPrerrequisitos(asignaturasCarrera);
    }

    public List<AsignaturaDTO> obtenerAsignaturasDTOConPrerrequisitos(List<Asignatura> asignaturasCarrera) {
        List<AsignaturaDTO> asignaturasCarreraDTO = new ArrayList<>();
        for(Asignatura asignatura : asignaturasCarrera) {
            List<Integer> codigosPrerrequisitos = prerrequisitoRepository.findAllByCodigoAsignatura(asignatura.getCodigo());

            if(codigosPrerrequisitos == null){
                codigosPrerrequisitos = new ArrayList<>();
            }

            AsignaturaDTO asignaturaDTO = Mapper.asignaturaToAsignaturaDTO(asignatura, codigosPrerrequisitos);

            asignaturasCarreraDTO.add(asignaturaDTO);
        }
        return asignaturasCarreraDTO;
    }
}
