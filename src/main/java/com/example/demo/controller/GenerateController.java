package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    public interface Service {
        String generateShortUrl(String original);
    }

    @Autowired
    private Service service;

    @PostMapping
    public GenerateResponse generate(@RequestBody @Valid GenerateRequest request) {
        return new GenerateResponse(service.generateShortUrl(request.getOriginal()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class GenerateRequest {
        @NotNull(message = "field original cannot be null")
        @NotBlank(message = "field original cannot be empty")
        @URL(message = "field original must be a valid URL address")
        private String original;
    }

    @Value
    public class GenerateResponse {
        private String link;
    }

}
