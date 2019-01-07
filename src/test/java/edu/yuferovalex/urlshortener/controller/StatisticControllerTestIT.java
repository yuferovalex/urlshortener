package edu.yuferovalex.urlshortener.controller;

import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticControllerTestIT {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnStatisticById() throws Exception {
        mvc.perform(get("/stats/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.link", is(equalTo("/l/1"))))
                .andExpect(jsonPath("$.original", is(equalTo("https://www.example.com/home"))))
                .andExpect(jsonPath("$.rank", is(equalTo(5))))
                .andExpect(jsonPath("$.count", is(equalTo(51979))));
    }

    @Test
    public void shouldReturnBadRequestIfIdHasWrongFormat() throws Exception {
        mvc.perform(get("/stats/_1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfLinkNotExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/stats/" + Base62.to(Integer.MAX_VALUE)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatisticByPage_firstPage() throws Exception {
        mvc.perform(get("/stats")
                .param("page", "0")
                .param("count", "10")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", is(equalTo(10))))
                .andExpect(jsonPath("$[0].rank", is(equalTo(1))))
                .andExpect(jsonPath("$[9].rank", is(equalTo(10))));
    }

    @Test
    public void shouldReturnStatisticByPage_lastPage() throws Exception {
        mvc.perform(get("/stats")
                .param("page", "1")
                .param("count", "10")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", is(equalTo(5))))
                .andExpect(jsonPath("$[0].rank", is(equalTo(11))))
                .andExpect(jsonPath("$[4].rank", is(equalTo(15))));
    }

    @Test
    public void shouldReturnBadRequestIfPageLessThanZero() throws Exception {
        mvc.perform(get("/stats")
                .param("page", "-1")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfPageSizeNotPositiveNumber() throws Exception {
        mvc.perform(get("/stats")
                .param("count", "0")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfPageSizeGreaterThan100() throws Exception {
        mvc.perform(get("/stats")
                .param("count", "101")
        )
                .andExpect(status().isBadRequest());
    }
}
