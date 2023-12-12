package com.usach.msasignaturas.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    Integer status;
    String message;

    public Response(Integer status, String message){
        this.status = status;
        this.message = message;
    }
}
