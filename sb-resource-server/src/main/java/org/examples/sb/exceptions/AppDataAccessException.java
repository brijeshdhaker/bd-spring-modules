package org.examples.sb.exceptions;

import org.examples.sb.models.AppRestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@RestControllerAdvice
public class AppDataAccessException {

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity<Object> handleDataAccessException() {
    AppRestResponse appResponse  = new AppRestResponse("Error while trying to access data.");
        return new ResponseEntity<>(appResponse,HttpStatus.BAD_REQUEST);
    }
}
