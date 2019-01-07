package edu.yuferovalex.urlshortener.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GenerateControllerTestIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    public void shouldWorkWithValidUrl() throws Exception {
        String body = "{ \"original\" : \"http://google.com/search?newwindow=1&q=СКБ Контур\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_UTF8)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.link", startsWith("/l/")));
    }

    @Test
    public void shouldNotAcceptEmptyUrl() throws Exception {
        String body = "{ \"original\" : \"\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_UTF8)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAcceptWrongUrlFormat() throws Exception {
        String body = "{ \"original\" : \"google.com\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_UTF8)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAcceptNotAUrl() throws Exception {
        String body = "{ \"original\" : \"bla-bla-bla\" }";
        mvc.perform(post("/generate")
                .contentType(APPLICATION_JSON_UTF8)
                .content(body)
        )
                .andExpect(status().isBadRequest());
    }
}
