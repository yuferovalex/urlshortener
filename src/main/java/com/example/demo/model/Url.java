package com.example.demo.model;

import com.example.demo.controller.RedirectController;
import com.example.demo.utils.Base62;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "url_redirects_index", columnList = "redirects DESC")
        }
)
public class Url {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer redirects = 0;

    @NotNull
    @Column(nullable = false, length = 2000)
    private String original;

    public Url(String original) {
        this.original = original;
    }

    public void increaseRedirects() {
        redirects += 1;
    }

    public String getLink() {
        return convertIdToLink(getId());
    }

    private static final String REDIRECT_PREFIX =
            RedirectController.class.getAnnotation(RequestMapping.class).value()[0].concat("/");

    public static String convertIdToLink(int id) {
        return REDIRECT_PREFIX.concat(Base62.to(id));
    }
}
