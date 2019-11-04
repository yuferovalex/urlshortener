package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.controller.dto.GenerateRequest;
import edu.yuferovalex.urlshortener.controller.dto.GenerateResponse;
import edu.yuferovalex.urlshortener.service.GeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/generate")
public class GenerateController {
    private final GeneratorService service;

    public GenerateController(GeneratorService service) {
        this.service = service;
    }

    @PostMapping
    GenerateResponse generate(@RequestBody @Valid GenerateRequest request) {
        return new GenerateResponse(
                RedirectController.REDIRECTION_PREFIX, service.generateShortUrl(request.getOriginal())
        );
    }
}
