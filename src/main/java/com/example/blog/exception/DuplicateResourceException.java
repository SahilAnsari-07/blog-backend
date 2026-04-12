package com.example.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException{
    private final Map<String , String > error;

        public DuplicateResourceException(Map<String , String > error){

            super("Registration data validation failed");
                this.error = error;
        }

    public Map<String, String> getError() {
        return error;
    }
}
