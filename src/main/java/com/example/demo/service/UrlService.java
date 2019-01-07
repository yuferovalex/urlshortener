package com.example.demo.service;

import com.example.demo.controller.GenerateController;
import com.example.demo.controller.RedirectController;
import com.example.demo.controller.StatisticController;
import com.example.demo.model.RankedUrl;
import com.example.demo.model.Url;
import com.example.demo.repository.UrlRepository;
import com.example.demo.utils.Base62;
import com.example.demo.utils.Base62Exception;
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
        return Base62.to(url.getId());
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
