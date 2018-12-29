package com.example.demo.model;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;

@Entity
public class Url {
    @Id
    @Getter
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(nullable = false, length = 2000)
    private String original;

    @Getter
    @Column(nullable = false)
    private Integer redirects = 0;

    public Url(@NonNull String original) {
        this.original = original;
    }

    public void increaseRedirects() {
        redirects += 1;
    }
}
