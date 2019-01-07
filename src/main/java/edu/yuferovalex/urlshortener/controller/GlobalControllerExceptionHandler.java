package edu.yuferovalex.urlshortener.controller;

import lombok.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @Value
    public class ErrorResponse {
        private String timestamp;
        private String path;
        private String message;
        private int status;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processValidationError(HttpServletRequest request, Exception exception) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        return new ErrorResponse(
                timestamp,
                request.getRequestURL().toString(),
                exception.getLocalizedMessage(),
                BAD_REQUEST.value()
        );
    }

}
