package com.example.restservice.model;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorDetail {
    private LocalDateTime timestamp;
    private String message;
    private String detail;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ErrorDetail(String message, String detail) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.detail = detail;
    }

    public void addFieldError(String objectName, String path, String message) {
        FieldError error = new FieldError(objectName, path, message);
        fieldErrors.add(error);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}

