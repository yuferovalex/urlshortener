package edu.yuferovalex.urlshortener.service;

import edu.yuferovalex.urlshortener.RankedUrlImpl;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.model.Url;
import edu.yuferovalex.urlshortener.repository.UrlRepository;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository repository;

    @InjectMocks
    private UrlService service;

    @Test
    void shouldGenerateNewShortUrls() {
        doAnswer(invocationOnMock -> {
            Url url = invocationOnMock.getArgument(0);
            url.setId(1);
            return null;
        }).when(repository).save(any());

        final String ACTUAL_SHORT_URL = service.generateShortUrl("https://kontur.ru");

        verify(repository).save(any());
        assertThat(ACTUAL_SHORT_URL, is("/l/" + Base62.to(1)));
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
        final RankedUrl EXPECTED_URL = new RankedUrlImpl(1, 1, 0, "https://kontur.ru");
        when(repository.findByIdWithRank(1))
                .thenReturn(Optional.of(EXPECTED_URL));

        final RankedUrl ACTUAL_URL = service.getRankedUrlByShortLink(Base62.to(1));

        assertThat(ACTUAL_URL, is(EXPECTED_URL));
        verify(repository).findByIdWithRank(1);
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
        final Pageable PAGE_REQUEST = PageRequest.of(1, 100);
        final Page<RankedUrl> PAGE = mock(Page.class);
        final Stream<RankedUrl> PAGE_DATA_STREAM = mock(Stream.class);
        final Iterable<RankedUrl> PAGE_DATA = mock(Iterable.class);

        when(repository.findAllWithRank(PAGE_REQUEST)).thenReturn(PAGE);
        when(PAGE.get()).thenReturn(PAGE_DATA_STREAM);
        when(PAGE_DATA_STREAM.collect(any())).thenReturn(PAGE_DATA);

        final Iterable<RankedUrl> ACTUAL_PAGE_DATA = service.getAllRankedUrl(PAGE_REQUEST);

        assertThat(ACTUAL_PAGE_DATA, is(PAGE_DATA));
        verify(repository).findAllWithRank(PAGE_REQUEST);
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
