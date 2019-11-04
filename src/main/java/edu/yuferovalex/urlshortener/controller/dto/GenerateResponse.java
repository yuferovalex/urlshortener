package edu.yuferovalex.urlshortener.controller.dto;

import lombok.Value;

@Value
public class GenerateResponse {
    private final String link;

    public GenerateResponse(String prefix, String link) {
        this.link = prefix + "/" + link;
    }
}
