package com.example.demo.model;

import com.example.demo.controller.RedirectController;
import com.example.demo.utils.Base62;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestMapping;

public interface RankedUrl {
    Integer getRank();
    String getOriginal();

    @JsonProperty("count")
    Integer getRedirects();

    @JsonIgnore
    Integer getId();

    default String getLink() {
        String prefix = RedirectController.class.getAnnotation(RequestMapping.class).value()[0];
        return prefix.concat("/").concat(Base62.to(getId()));
    }
}
