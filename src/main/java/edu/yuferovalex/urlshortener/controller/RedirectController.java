package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.model.Url;
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
        Url getUrlByLink(String link);
        void increaseRedirects(Url url);
    }

    @Autowired
    private Service service;

    @GetMapping("/{link}")
    public RedirectView redirect(@PathVariable String link) {
        Url url = service.getUrlByLink(link);
        service.increaseRedirects(url);
        return new RedirectView(url.getOriginal());
    }

}
