package com.example.restservice.handler;

import com.example.restservice.exception.InternalServerException;
import com.example.restservice.exception.ThrottledException;
import com.example.restservice.model.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    //if the entered url path is incorrect
    @ExceptionHandler(ThrottledException.class)
    public ResponseEntity<ErrorDetail> throttledExceptionHandler(Exception e, WebRequest request) {
        ErrorDetail error = new ErrorDetail(e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, TOO_MANY_REQUESTS);
    }

    @ExceptionHandler({InternalServerException.class, Exception.class})
    public ResponseEntity<ErrorDetail> internalServerExceptionHandler(Exception e, WebRequest request) {
        ErrorDetail error = new ErrorDetail(e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }
}
