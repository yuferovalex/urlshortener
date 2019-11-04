package edu.yuferovalex.urlshortener.integration;

import edu.yuferovalex.urlshortener.utils.Base62;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedirectControllerTestIT extends BaseTestIT {
    @Test
    @Transactional
    void shouldRedirect() throws Exception {
        mvc.perform(get("/l/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldNotAcceptWrongParameterFormat() throws Exception {
        mvc.perform(get("/l/_1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfLinkNotExists() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/l/" + Base62.to(Integer.MAX_VALUE)))
                .andExpect(status().isNotFound());
    }
}
