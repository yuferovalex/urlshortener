package com.example.demo.service;

import com.example.demo.RankedUrlImpl;
import com.example.demo.model.RankedUrl;
import com.example.demo.model.Url;
import com.example.demo.repository.UrlRepository;
import com.example.demo.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository repository;

    @InjectMocks
    private UrlService service;

    @Test
    public void shouldGenerateNewShortUrls() {
        doAnswer(invocationOnMock -> {
            Url url = invocationOnMock.getArgument(0);
            url.setId(1);
            return null;
        }).when(repository).save(any(Url.class));

        final String ACTUAL_SHORT_URL = service.generateShortUrl("https://kontur.ru");

        verify(repository).save(any(Url.class));
        assertThat(ACTUAL_SHORT_URL, is(Base62.to(1)));
    }

    @Test
    public void shouldReturnOriginalUrlByShortLink() {
        final Url EXPECTED_URL = new Url(1, 0, "https://kontur.ru");
        when(repository.findById(1))
                .thenReturn(Optional.of(EXPECTED_URL));

        final String ACTUAL_URL = service.getOriginalUrl(Base62.to(1));

        verify(repository).findById(1);
        assertEquals(ACTUAL_URL, EXPECTED_URL.getOriginal());
    }

    @Test(expected = LinkNotFoundException.class)
    public void getOriginalUrlShouldThrowIfLinkNotPresentedInRepository() {
        when(repository.findById(1))
                .thenReturn(Optional.empty());

        service.getOriginalUrl(Base62.to(1));
    }

    @Test
    public void shouldReturnRankedUrlByShortLink() {
        final RankedUrl EXPECTED_URL = new RankedUrlImpl(1, 1, 0, "https://kontur.ru");
        when(repository.findByIdWithRank(1))
                .thenReturn(Optional.of(EXPECTED_URL));

        final RankedUrl ACTUAL_URL = service.getRankedUrlByShortLink(Base62.to(1));

        assertEquals(EXPECTED_URL, ACTUAL_URL);
        verify(repository).findByIdWithRank(1);
    }

    @Test(expected = LinkNotFoundException.class)
    public void getRankedUrlByShortLinkShouldThrowIfLinkNotPresentedInRepository() {
        when(repository.findByIdWithRank(1))
                .thenReturn(Optional.empty());

        service.getRankedUrlByShortLink(Base62.to(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnAllRankedUrl() {
        final Pageable PAGE_REQUEST = PageRequest.of(1, 100);
        final Page<RankedUrl> PAGE = mock(Page.class);
        final Stream<RankedUrl> PAGE_DATA_STREAM = mock(Stream.class);
        final Iterable<RankedUrl> PAGE_DATA = mock(Iterable.class);

        when(repository.findAllWithRank(PAGE_REQUEST)).thenReturn(PAGE);
        when(PAGE.get()).thenReturn(PAGE_DATA_STREAM);
        when(PAGE_DATA_STREAM.collect(any())).thenReturn(PAGE_DATA);

        final Iterable<RankedUrl> ACTUAL_PAGE_DATA = service.getAllRankedUrl(PAGE_REQUEST);

        assertSame(PAGE_DATA, ACTUAL_PAGE_DATA);
        verify(repository).findAllWithRank(PAGE_REQUEST);
    }

    @Test(expected = WrongLinkException.class)
    public void getOriginalUrlShouldThrowIfWrongLinkFormat() {
        service.getOriginalUrl("_asd_");
    }

    @Test(expected = WrongLinkException.class)
    public void getRankedUrlByShortLinkShouldThrowIfWrongLinkFormat() {
        service.getRankedUrlByShortLink("_asd_");
    }

}
