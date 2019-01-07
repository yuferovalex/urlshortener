package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.service.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedirectControllerTest {
    @Mock
    private UrlService service;

    @InjectMocks
    private RedirectController controller;

    final String SHORT_URL_ID = Base62.to(1);
    final String ORIGINAL_URL = "https://kontur.ru";

    @Test
    public void shouldRedirectToOriginalUrl() {
        when(service.getOriginalUrlAndIncreaseRedirects(SHORT_URL_ID)).thenReturn(ORIGINAL_URL);

        RedirectView redirectView = controller.redirect(SHORT_URL_ID);

        assertEquals(redirectView.getUrl(), ORIGINAL_URL);
        verify(service).getOriginalUrlAndIncreaseRedirects(SHORT_URL_ID);
    }
}