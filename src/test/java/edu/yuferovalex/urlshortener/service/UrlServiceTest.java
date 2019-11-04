package edu.yuferovalex.urlshortener.service;

import edu.yuferovalex.urlshortener.RankedUrlImpl;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.model.Url;
import edu.yuferovalex.urlshortener.repository.UrlRepository;
import edu.yuferovalex.urlshortener.service.impl.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository repository;

    @InjectMocks
    private UrlService service;

    private static final RankedUrlImpl RANKED_URL = new RankedUrlImpl(1, 1, 0, "https://kontur.ru");

    @Test
    void shouldGenerateNewShortUrls() {
        doAnswer(invocationOnMock -> {
            Url url = invocationOnMock.getArgument(0);
            url.setId(1);
            return null;
        }).when(repository).save(any());

        String actual = service.generateShortUrl("https://kontur.ru");

        verify(repository).save(any());
        assertThat(actual, is(Base62.to(1)));
    }

    @Test
    void shouldDoRedirect() {
        String expectedUrl = "http://kontur.ru";
        Url url = mock(Url.class);
        when(repository.findById(1))
                .thenReturn(Optional.of(url));
        when(url.getOriginal())
                .thenReturn(expectedUrl);
        String actualUrl = service.doRedirect("1");

        assertThat(expectedUrl, is(actualUrl));
        verify(repository).findById(1);
        verify(repository).increaseRedirectsCount(1);
    }

    @Test
    void doRedirectShouldThrowIfLinkNotPresentedInRepository() {
        assertThrows(LinkNotFoundException.class, () -> {
            when(repository.findById(1))
                    .thenReturn(Optional.empty());

            service.doRedirect("1");
        });
    }

    @Test
    void shouldReturnRankedUrlByShortLink() {
        UrlStatistic expected = new UrlStatistic(RANKED_URL, Base62.to(1));
        when(repository.findByIdWithRank(1))
                .thenReturn(Optional.of(RANKED_URL));

        UrlStatistic actual = service.getRankedUrlByShortLink(Base62.to(1));

        verify(repository).findByIdWithRank(1);
        assertThat(actual.getCount(), is(RANKED_URL.getRedirects()));
        assertThat(actual.getOriginal(), is(RANKED_URL.getOriginal()));
        assertThat(actual.getRank(), is(RANKED_URL.getRank()));
        assertThat(actual.getLink(), is(Base62.to(RANKED_URL.getId())));
    }

    @Test
    void getRankedUrlByShortLinkShouldThrowIfLinkNotPresentedInRepository() {
        assertThrows(LinkNotFoundException.class, () -> {
            when(repository.findByIdWithRank(1))
                    .thenReturn(Optional.empty());

            service.getRankedUrlByShortLink(Base62.to(1));
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnAllRankedUrl() {
        Pageable pageRequest = PageRequest.of(1, 1);
        Page<RankedUrl> page = mock(Page.class);

        when(repository.findAllWithRank(pageRequest)).thenReturn(page);
        when(page.get()).thenReturn(Stream.of(RANKED_URL));

        List<UrlStatistic> actual = service.getAllRankedUrl(pageRequest);

        verify(repository).findAllWithRank(pageRequest);
        assertThat(actual, hasSize(1));
        UrlStatistic urlStatistic = actual.get(0);
        assertThat(urlStatistic.getCount(), is(RANKED_URL.getRedirects()));
        assertThat(urlStatistic.getOriginal(), is(RANKED_URL.getOriginal()));
        assertThat(urlStatistic.getRank(), is(RANKED_URL.getRank()));
        assertThat(urlStatistic.getLink(), is(Base62.to(RANKED_URL.getId())));
    }

    @Test
    void doRedirectShouldThrowIfWrongLinkFormat() {
        assertThrows(WrongLinkException.class, () -> service.doRedirect("_asd_"));
    }

    @Test
    void getRankedUrlByShortLinkShouldThrowIfWrongLinkFormat() {
        assertThrows(WrongLinkException.class, () -> service.getRankedUrlByShortLink("_asd_"));
    }
}
