package edu.yuferovalex.urlshortener;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import lombok.Value;

@Value
public class RankedUrlImpl implements RankedUrl {
    private Integer id;
    private Integer rank;
    private Integer redirects;
    private String original;
}
