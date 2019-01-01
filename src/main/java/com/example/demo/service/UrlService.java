package com.example.demo.service;

import com.example.demo.model.RankedUrl;
import com.example.demo.model.Url;
import com.example.demo.repository.UrlRepository;
import com.example.demo.utils.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UrlService {

    @Autowired
    private UrlRepository repository;

    public String generateShortUrl(String original) {
        Url url = new Url(original);
        repository.save(url);
        return Base62.to(url.getId());
    }

    public String getOriginalUrl(String link) {
        return repository
                .findById(Base62.from(link))
                .orElseThrow(() -> new LinkNotFoundException(String.format("link '%s' not found", link)))
                .getOriginal();
    }

    public RankedUrl getRankedUrlByShortLink(String link) {
        return repository
                .findByIdWithRank(Base62.from(link))
                .orElseThrow(() -> new LinkNotFoundException(String.format("link '%s' not found", link)));
    }

    public Iterable<RankedUrl> getAllRankedUrl(Pageable pageable) {
        return repository.findAllWithRank(pageable).get()
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
