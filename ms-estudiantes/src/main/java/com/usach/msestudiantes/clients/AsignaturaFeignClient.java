package com.usach.msestudiantes.clients;

import com.usach.msestudiantes.configurations.FeignClientConfig;
import com.usach.msestudiantes.dtos.AsignaturaDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "API-ASIGNATURAS", url = "http://localhost:8080", configuration = FeignClientConfig.class)
public interface AsignaturaFeignClient {
    @GetMapping(value = "/api/asignaturas")
    List<AsignaturaDTO> obtenerAsignaturas();

    @GetMapping(value = "/api/asignaturas/{codigoCarrera}")
    List<AsignaturaDTO> obtenerPlanEstudios(@PathVariable("codigoCarrera") Integer codigoCarrera);
}
