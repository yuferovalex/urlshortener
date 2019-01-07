package com.example.demo.controller;

import com.example.demo.service.UrlService;
import com.example.demo.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenerateControllerTest {

    @Mock
    private UrlService service;

    @InjectMocks
    private GenerateController controller;

    private final String SHORT_URL_ID = "/l/" + Base62.to(1);
    private final String ORIGINAL_URL = "https://kontur.ru";

    @Test
    public void shouldGenerateShortUrls() {
        when(service.generateShortUrl(ORIGINAL_URL)).thenReturn(SHORT_URL_ID);
        GenerateController.GenerateRequest request = new GenerateController.GenerateRequest(ORIGINAL_URL);

        GenerateController.GenerateResponse response = controller.generate(request);

        assertEquals(SHORT_URL_ID, response.getLink());
        verify(service).generateShortUrl(ORIGINAL_URL);
    }
}