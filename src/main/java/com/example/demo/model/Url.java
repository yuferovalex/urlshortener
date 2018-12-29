package com.example.demo.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(
        indexes = {
                @Index(name = "IX_URL_REDIRECTS", columnList = "redirects DESC")
        }
)
public class Url {
    @Id
    @Getter
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue
    private Integer id;

    @Getter
    @Column(nullable = false, length = 2000)
    private String original;

    @Getter
    @Column(nullable = false)
    private Integer redirects;

    public void increaseRedirects() {
        redirects += 1;
    }
}
