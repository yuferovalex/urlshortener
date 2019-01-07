package edu.yuferovalex.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface RankedUrl {
    Integer getRank();
    String getOriginal();

    @JsonProperty("count")
    Integer getRedirects();

    @JsonIgnore
    Integer getId();

    default String getLink() {
        return Url.convertIdToLink(getId());
    }
}
