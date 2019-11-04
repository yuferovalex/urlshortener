package edu.yuferovalex.urlshortener.service.impl;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.model.Url;
import edu.yuferovalex.urlshortener.repository.UrlRepository;
import edu.yuferovalex.urlshortener.service.*;
import edu.yuferovalex.urlshortener.utils.Base62;
import edu.yuferovalex.urlshortener.utils.Base62Exception;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlService implements RedirectionService, GeneratorService, StatisticService {
    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateShortUrl(String original) {
        Url url = new Url(original);
        repository.save(url);
        return Base62.to(url.getId());
    }

    @Override
    public String doRedirect(String link) {
        try {
            int id = Base62.from(link);
            String originalUrl = repository
                    .findById(id)
                    .orElseThrow(() -> new LinkNotFoundException(link))
                    .getOriginal();
            repository.increaseRedirectsCount(id);
            return originalUrl;
        } catch (Base62Exception e) {
            throw new WrongLinkException(link, e);
        }
    }

    @Override
    public UrlStatistic getRankedUrlByShortLink(String link) {
        try {
            return repository
                    .findByIdWithRank(Base62.from(link))
                    .map(this::convertRankedUrlToUrlStatistic)
                    .orElseThrow(() -> new LinkNotFoundException(link));
        } catch (Base62Exception e) {
            throw new WrongLinkException(link, e);
        }
    }

    @Override
    public List<UrlStatistic> getAllRankedUrl(Pageable pageable) {
        return repository.findAllWithRank(pageable).get()
                .map(this::convertRankedUrlToUrlStatistic)
                .collect(Collectors.toList());
    }

    private UrlStatistic convertRankedUrlToUrlStatistic(RankedUrl url) {
        return new UrlStatistic(url, Base62.to(url.getId()));
    }
}
