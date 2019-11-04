package edu.yuferovalex.urlshortener.repository;

import edu.yuferovalex.urlshortener.RankedUrlImpl;
import edu.yuferovalex.urlshortener.model.RankedUrl;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository repository;

    @Test
    void findByIdWithRankTest() {
        data().forEach(expected -> {
            Optional<RankedUrl> urlOptional = repository.findByIdWithRank(expected.getId());
            assertTrue(urlOptional.isPresent());
            RankedUrl url = urlOptional.get();
            assertThat(url, ulrAreEqual(expected));
        });
    }

    @Test
    void findAllWithRankTest() {
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
        // Data form data.sql ordered by rank.
        return Arrays.asList(
                new RankedUrlImpl(15, 1, 96520, "https://vk.com/id0"),
                new RankedUrlImpl(6, 2, 82871, "https://ya.ru"),
                new RankedUrlImpl(3, 3, 77205, "https://www.example.com/news"),
                new RankedUrlImpl(9, 4, 59070, "https://google.ru"),
                new RankedUrlImpl(1, 5, 51979, "https://www.example.com/home"),
                new RankedUrlImpl(14, 6, 37663, "https://vk.com/blog"),
                new RankedUrlImpl(2, 7, 37549, "https://www.example.com/about"),
                new RankedUrlImpl(10, 8, 24588, "https://kontur.ru"),
                new RankedUrlImpl(5, 9, 24129, "https://yandex.ru"),
                new RankedUrlImpl(8, 10, 23152, "https://google.com"),
                new RankedUrlImpl(11, 11, 20873, "https://vk.com"),
                new RankedUrlImpl(4, 12, 11413, "https://www.example.com/search?q=something"),
                new RankedUrlImpl(12, 13, 7789, "https://vk.com/feed"),
                new RankedUrlImpl(13, 14, 5579, "https://vk.com/im"),
                new RankedUrlImpl(7, 15, 241, "https://google.ru")
        );
    }
}


