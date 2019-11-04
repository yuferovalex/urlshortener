package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.RankedUrlImpl;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.service.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {

    @Mock
    private UrlService service;

    @InjectMocks
    private StatisticController controller;

    private final String SHORT_URL_ID = Base62.to(1);
    private final String ORIGINAL_URL = "https://kontur.ru";

    @Test
    void shouldReturnStatisticById() {
        final RankedUrl EXPECTED_RANKED_URL = new RankedUrlImpl(1, 2, 10, ORIGINAL_URL);
        when(service.getRankedUrlByShortLink(SHORT_URL_ID)).thenReturn(EXPECTED_RANKED_URL);

        final RankedUrl ACTUAL_RANKED_URL = controller.getStatisticById(SHORT_URL_ID);

        assertThat(ACTUAL_RANKED_URL, is(EXPECTED_RANKED_URL));
        verify(service).getRankedUrlByShortLink(SHORT_URL_ID);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnStatistic() {
        final Pageable PAGE_REQUEST = PageRequest.of(1, 100);
        final Iterable<RankedUrl> EXPECTED_PAGE_DATA = mock(Iterable.class);
        when(service.getAllRankedUrl(PAGE_REQUEST))
                .thenReturn(EXPECTED_PAGE_DATA);

        final Iterable<RankedUrl> ACTUAL_PAGE_DATA = controller.getStatistic(1, 100);

        assertThat(ACTUAL_PAGE_DATA, is(EXPECTED_PAGE_DATA));
        verify(service).getAllRankedUrl(PAGE_REQUEST);
    }
}