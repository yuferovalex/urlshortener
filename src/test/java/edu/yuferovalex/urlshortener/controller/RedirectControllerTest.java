package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.model.Url;
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

    @Test
    public void shouldRedirectToOriginalUrl() {
        String shortUrlId = Base62.to(1);
        Url url = new Url(1, 10, "https://kontur.ru");
        when(service.getUrlByLink(shortUrlId)).thenReturn(url);

        RedirectView redirectView = controller.redirect(shortUrlId);

        assertEquals(redirectView.getUrl(), url.getOriginal());
        verify(service).getUrlByLink(shortUrlId);
    }
}