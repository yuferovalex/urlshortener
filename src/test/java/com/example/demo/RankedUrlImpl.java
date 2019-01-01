package com.example.demo;

import com.example.demo.model.RankedUrl;
import lombok.Value;

@Value
public class RankedUrlImpl implements RankedUrl {
    private Integer id;
    private Integer rank;
    private Integer redirects;
    private String original;
}
