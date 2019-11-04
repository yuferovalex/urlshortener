package edu.yuferovalex.urlshortener.model;

public interface RankedUrl {
    int getRank();
    String getOriginal();

    int getRedirects();

    int getId();
}
