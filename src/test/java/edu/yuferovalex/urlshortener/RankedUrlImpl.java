package edu.yuferovalex.urlshortener;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import lombok.Value;

@Value
public class RankedUrlImpl implements RankedUrl {
    private int id;
    private int rank;
    private int redirects;
    private String original;
}
