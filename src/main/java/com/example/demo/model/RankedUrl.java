package com.example.demo.model;

import com.example.demo.utils.RankedUrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RankedUrlSerializer.class)
public interface RankedUrl {
    Integer getId();

    Integer getRank();

    Integer getRedirects();

    String getOriginal();
}
