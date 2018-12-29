package com.example.demo.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class Base62TranslationTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // Base 10  | Base 62
                { 0,          "0"      },
                { 68,         "16"     },
                { 235,        "3N"     },
                { 543,        "8L"     },
                { 9786,       "2xQ"    },
                { 40323,      "aun"    },
                { 123456,     "w7e"    },
                { 123432,     "w6Q"    },
                { 4532521,    "j17b"   },
                { 2147483647, "2lkCB1" }
            }
        );
    }

    @Parameter(0)
    public Integer base10;

    @Parameter(1)
    public String base62;

    @Test
    public void from() {
        assertThat(Base62.from(base62), is(base10));
    }

    @Test
    public void to() {
        assertThat(Base62.to(base10), is(base62));
    }
}