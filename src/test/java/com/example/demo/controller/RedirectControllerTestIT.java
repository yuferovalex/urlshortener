package com.example.demo.controller;

import com.example.demo.utils.Base62;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedirectControllerTestIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    public void shouldRedirect() throws Exception {
        mvc.perform(get("/l/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldNotAcceptWrongParameterFormat() throws Exception {
        mvc.perform(get("/l/_1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfLinkNotExists() throws Exception {
        mvc.perform(get("/l/" + Base62.to(Integer.MAX_VALUE)))
                .andExpect(status().isNotFound());
    }

}
