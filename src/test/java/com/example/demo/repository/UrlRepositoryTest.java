package com.example.demo.repository;

import com.example.demo.model.RankedUrl;
import lombok.Value;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository repository;

    @Test
    public void findByIdWithRankTest() {
        data().forEach(expected -> {
            RankedUrl url = repository.findByIdWithRank(expected.getId());
            assertThat(url, ulrAreEqual(expected));
        });
    }

    @Test
    public void findAllWithRankTest() {
        Collection<RankedUrl> data = data();
        final int PAGE_SIZE = 10;
        final int PAGE_COUNT = (int) Math.ceil(1.0 * data.size() / PAGE_SIZE);
        for (int page = 0; page < PAGE_COUNT; ++page) {
            Iterator<RankedUrl> expectedData = data.stream().skip(PAGE_SIZE * page).limit(PAGE_SIZE).iterator();
            findAllWithRankTestBody(PageRequest.of(page, PAGE_SIZE), expectedData);
        }
    }

    private void findAllWithRankTestBody(Pageable pageable, Iterator<RankedUrl> expectedData) {
        Iterator<RankedUrl> actualData = repository.findAllWithRank(pageable).get().iterator();
        while (expectedData.hasNext() && actualData.hasNext()) {
            assertThat(actualData.next(), ulrAreEqual(expectedData.next()));
        }
        assertEquals(expectedData.hasNext(), actualData.hasNext());
    }

    private Matcher<RankedUrl> ulrAreEqual(RankedUrl expected) {
        return new BaseMatcher<RankedUrl>() {
            @Override
            public boolean matches(final Object o) {
                RankedUrl actual = (RankedUrl) o;
                return expected.getId().equals(actual.getId()) &&
                        expected.getRank().equals(actual.getRank()) &&
                        expected.getRedirects().equals(actual.getRedirects()) &&
                        expected.getOriginal().equals(actual.getOriginal());
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("must be equal to ").appendValue(expected);
            }
        };
    }

    static public Collection<RankedUrl> data() {
        @Value
        class RankedUrlImpl implements RankedUrl {
            private Integer id;
            private Integer rank;
            private Integer redirects;
            private String original;
        }

        // Data form data.sql ordered by rank.
        return Arrays.asList(
                new RankedUrlImpl(15, 1, 96520, "15"),
                new RankedUrlImpl(6, 2, 82871, "6"),
                new RankedUrlImpl(3, 3, 77205, "3"),
                new RankedUrlImpl(9, 4, 59070, "9"),
                new RankedUrlImpl(1, 5, 51979, "1"),
                new RankedUrlImpl(14, 6, 37663, "14"),
                new RankedUrlImpl(2, 7, 37549, "2"),
                new RankedUrlImpl(10, 8, 24588, "10"),
                new RankedUrlImpl(5, 9, 24129, "5"),
                new RankedUrlImpl(8, 10, 23152, "8"),
                new RankedUrlImpl(11, 11, 20873, "11"),
                new RankedUrlImpl(4, 12, 11413, "4"),
                new RankedUrlImpl(12, 13, 7789, "12"),
                new RankedUrlImpl(13, 14, 5579, "13"),
                new RankedUrlImpl(7, 15, 241, "7")
        );
    }
}


