package edu.yuferovalex.urlshortener.service;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import lombok.Value;

@Value
public class UrlStatistic {
    private final int rank;
    private final int count;
    private final String original;
    private final String link;

    public UrlStatistic(RankedUrl rankedUrl, String link) {
        this.rank = rankedUrl.getRank();
        this.count = rankedUrl.getRedirects();
        this.original = rankedUrl.getOriginal();
        this.link = link;
    }
}
