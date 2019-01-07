package edu.yuferovalex.urlshortener.service;

import edu.yuferovalex.urlshortener.controller.GenerateController;
import edu.yuferovalex.urlshortener.controller.RedirectController;
import edu.yuferovalex.urlshortener.controller.StatisticController;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.model.Url;
import edu.yuferovalex.urlshortener.repository.UrlRepository;
import edu.yuferovalex.urlshortener.utils.Base62;
import edu.yuferovalex.urlshortener.utils.Base62Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UrlService implements
        RedirectController.Service,
        GenerateController.Service,
        StatisticController.Service
{

    @Autowired
    private UrlRepository repository;

    @Override
    public String generateShortUrl(String original) {
        Url url = new Url(original);
        repository.save(url);
        return url.getLink();
    }

    @Override
    public String getOriginalUrlAndIncreaseRedirects(String link) {
        try {
            Url url = repository
                    .findById(Base62.from(link))
                    .orElseThrow(() -> new LinkNotFoundException(link));
            url.increaseRedirects();
            repository.save(url);
            return url.getOriginal();
        } catch (Base62Exception e) {
            throw new WrongLinkException(link, e);
        }
    }

    @Override
    public RankedUrl getRankedUrlByShortLink(String link) {
        try {
            return repository
                    .findByIdWithRank(Base62.from(link))
                    .orElseThrow(() -> new LinkNotFoundException(link));
        } catch (Base62Exception e) {
            throw new WrongLinkException(link, e);
        }
    }

    @Override
    public Iterable<RankedUrl> getAllRankedUrl(Pageable pageable) {
        return repository.findAllWithRank(pageable).get()
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
