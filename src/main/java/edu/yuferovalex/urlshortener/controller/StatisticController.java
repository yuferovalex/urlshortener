package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.controller.dto.UrlStatisticResponse;
import edu.yuferovalex.urlshortener.service.StatisticService;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/stats")
public class StatisticController {
    private final StatisticService service;

    public StatisticController(StatisticService service) {
        this.service = service;
    }

    @GetMapping
    List<UrlStatisticResponse> getStatistic(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "100") @Min(1) @Max(100) Integer count
    ) {
        return service.getAllRankedUrl(PageRequest.of(page, count)).stream()
                .map(stat -> new UrlStatisticResponse(RedirectController.REDIRECTION_PREFIX, stat))
                .collect(Collectors.toList());
    }

    @GetMapping("/{link}")
    UrlStatisticResponse getStatisticById(@PathVariable String link) {
        return new UrlStatisticResponse(RedirectController.REDIRECTION_PREFIX, service.getRankedUrlByShortLink(link));
    }
}
