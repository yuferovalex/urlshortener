package edu.yuferovalex.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/l")
public class RedirectController {

    public interface Service {
        String getOriginalUrlAndIncreaseRedirects(String link);
    }

    @Autowired
    private Service service;

    @GetMapping("/{link}")
    public RedirectView redirect(@PathVariable String link) {
        return new RedirectView(service.getOriginalUrlAndIncreaseRedirects(link));
    }

}