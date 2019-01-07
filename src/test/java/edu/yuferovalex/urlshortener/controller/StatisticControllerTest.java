package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.RankedUrlImpl;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.service.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatisticControllerTest {

    @Mock
    private UrlService service;

    @InjectMocks
    private StatisticController controller;

    final String SHORT_URL_ID = Base62.to(1);
    final String ORIGINAL_URL = "https://kontur.ru";

    @Test
    public void shouldReturnStatisticById() {
        final RankedUrl EXPECTED_RANKED_URL = new RankedUrlImpl(1, 2, 10, ORIGINAL_URL);
        when(service.getRankedUrlByShortLink(SHORT_URL_ID)).thenReturn(EXPECTED_RANKED_URL);

        final RankedUrl ACTUAL_RANKED_URL = controller.getStatisticById(SHORT_URL_ID);

        assertSame(EXPECTED_RANKED_URL, ACTUAL_RANKED_URL);
        verify(service).getRankedUrlByShortLink(SHORT_URL_ID);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnStatistic() {
        final Pageable PAGE_REQUEST = PageRequest.of(1, 100);
        final Iterable<RankedUrl> EXPECTED_PAGE_DATA = mock(Iterable.class);
        when(service.getAllRankedUrl(PAGE_REQUEST))
                .thenReturn(EXPECTED_PAGE_DATA);

        final Iterable<RankedUrl> ACTUAL_PAGE_DATA = controller.getStatistic(1, 100);

        assertSame(EXPECTED_PAGE_DATA, ACTUAL_PAGE_DATA);
        verify(service).getAllRankedUrl(PAGE_REQUEST);
    }
}