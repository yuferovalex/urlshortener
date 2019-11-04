package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.service.UrlService;
import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateControllerTest {

    @Mock
    private UrlService service;

    @InjectMocks
    private GenerateController controller;

    private final String SHORT_URL_ID = "/l/" + Base62.to(1);
    private final String ORIGINAL_URL = "https://kontur.ru";

    @Test
    void shouldGenerateShortUrls() {
        when(service.generateShortUrl(ORIGINAL_URL)).thenReturn(SHORT_URL_ID);
        GenerateController.GenerateRequest request = new GenerateController.GenerateRequest(ORIGINAL_URL);

        GenerateController.GenerateResponse response = controller.generate(request);

        assertThat(response.getLink(), is(SHORT_URL_ID));
        verify(service).generateShortUrl(ORIGINAL_URL);
    }
}