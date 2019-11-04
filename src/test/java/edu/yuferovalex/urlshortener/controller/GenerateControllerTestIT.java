package edu.yuferovalex.urlshortener.controller;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GenerateControllerTestIT extends BaseControllerTestIT {
    @Test
    @Transactional
    void shouldWorkWithValidUrl() throws Exception {
        String body = "{ \"original\" : \"http://google.com/search?newwindow=1&q=СКБ Контур\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_VALUE)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.link", startsWith("/l/")));
    }

    @Test
    void shouldNotAcceptEmptyUrl() throws Exception {
        String body = "{ \"original\" : \"\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_VALUE)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAcceptWrongUrlFormat() throws Exception {
        String body = "{ \"original\" : \"google.com\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_VALUE)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAcceptNotAUrl() throws Exception {
        String body = "{ \"original\" : \"bla-bla-bla\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_VALUE)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }
}
