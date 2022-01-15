package com.example.exception.handler;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.FORBIDDEN, ex.getClass(), ex.getMessage());
        return  new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handledException(Exception ex) {
        ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus httpStatus = status != null ? status.code() : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorInfo errorInfo = new ErrorInfo(httpStatus, ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), httpStatus);
    }
}
