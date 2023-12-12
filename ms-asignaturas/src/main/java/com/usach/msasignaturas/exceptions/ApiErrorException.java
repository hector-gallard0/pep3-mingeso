package com.usach.msasignaturas.exceptions;

public class ApiErrorException extends RuntimeException {
    public ApiErrorException(String message){
        super(message);
    }
}
