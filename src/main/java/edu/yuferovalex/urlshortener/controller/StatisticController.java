package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping("/stats")
public class StatisticController {

    public interface Service {
        Iterable<RankedUrl> getAllRankedUrl(Pageable pageable);
        RankedUrl getRankedUrlByShortLink(String link);
    }

    @Autowired
    private Service service;

    @GetMapping
    public Iterable<RankedUrl> getStatistic(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "100") @Min(1) @Max(100) Integer count
    ) {
        return service.getAllRankedUrl(PageRequest.of(page, count));
    }

    @GetMapping("/{link}")
    public RankedUrl getStatisticById(@PathVariable String link) {
        return service.getRankedUrlByShortLink(link);
    }

}
