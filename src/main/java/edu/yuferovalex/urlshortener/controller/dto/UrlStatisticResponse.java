package edu.yuferovalex.urlshortener.controller.dto;

import edu.yuferovalex.urlshortener.service.UrlStatistic;
import lombok.ToString;

@ToString
public class UrlStatisticResponse {
    private final UrlStatistic statistic;
    private final String urlPrefix;

    public UrlStatisticResponse(String urlPrefix, UrlStatistic statistic) {
        this.statistic = statistic;
        this.urlPrefix = urlPrefix;
    }

    public int getRank() {
        return statistic.getRank();
    }

    public int getCount() {
        return statistic.getCount();
    }

    public String getOriginal() {
        return statistic.getOriginal();
    }

    public String getLink() {
        return urlPrefix + "/" + statistic.getLink();
    }
}
