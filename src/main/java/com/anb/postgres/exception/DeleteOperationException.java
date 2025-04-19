package com.anb.postgres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeleteOperationException extends RuntimeException{
    public DeleteOperationException(String message){
        super(message);
    }
}
