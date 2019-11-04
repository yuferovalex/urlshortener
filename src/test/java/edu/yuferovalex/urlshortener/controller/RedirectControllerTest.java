package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.model.Url;
import edu.yuferovalex.urlshortener.service.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedirectControllerTest {
    @Mock
    private UrlService service;

    @InjectMocks
    private RedirectController controller;

    @Test
    void shouldRedirectToOriginalUrl() {
        String shortUrlId = Base62.to(1);
        Url url = new Url(1, 10, "https://kontur.ru");
        when(service.doRedirect(shortUrlId)).thenReturn(url.getOriginal());

        RedirectView redirectView = controller.redirect(shortUrlId);

        assertThat(redirectView.getUrl(), is(url.getOriginal()));
        verify(service).doRedirect(shortUrlId);
    }
}