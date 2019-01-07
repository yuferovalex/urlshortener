package edu.yuferovalex.urlshortener.controller;

import lombok.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value
    class ErrorResponse {
        private String timestamp;
        private String path;
        private String message;
        private int status;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> processValidationError(HttpServletRequest request, Exception exception) {
        return createErrorResponse(request, BAD_REQUEST, exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest request, Exception exception) {
        ResponseStatus annotation = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        HttpStatus status = annotation == null
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : annotation.value();
        return createErrorResponse(request, status, exception);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            HttpServletRequest request,
            HttpStatus status,
            Exception exception
    ) {
        if (status.is5xxServerError()) {
            logger.error("unhandled exception caught", exception);
        } else {
            logger.warn(exception.getClass().getName() + ": " + exception.getLocalizedMessage());
        }
        return ResponseEntity.status(status).body(new ErrorResponse(
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                request.getRequestURL().toString(),
                exception.getLocalizedMessage(),
                status.value()
        ));
    }

}
