package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Url {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer redirects = 0;

    @Column(nullable = false, length = 2000)
    private String original;

    public Url(@NonNull String original) {
        this.original = original;
    }

    public void increaseRedirects() {
        redirects += 1;
    }
}
