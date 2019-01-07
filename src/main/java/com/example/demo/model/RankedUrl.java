package com.example.demo.model;

import com.example.demo.controller.RedirectController;
import com.example.demo.utils.Base62;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.RequestMapping;

public interface RankedUrl {
    Integer getRank();
    Integer getRedirects();
    String getOriginal();

    @JsonIgnore
    Integer getId();

    default String getLink() {
        String prefix = RedirectController.class.getAnnotation(RequestMapping.class).value()[0];
        return prefix.concat("/").concat(Base62.to(getId()));
    }
}
