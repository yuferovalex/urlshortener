package edu.yuferovalex.urlshortener.repository;

import edu.yuferovalex.urlshortener.model.RankedUrl;
import edu.yuferovalex.urlshortener.model.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {

    @Query(
            value = "SELECT *\n" +
                    "FROM (\n" +
                    "    SELECT ROWNUM() AS rank, u1.* \n" +
                    "    FROM (\n" +
                    "        SELECT * \n" +
                    "        FROM url \n" +
                    "        ORDER BY redirects DESC\n" +
                    "    ) AS u1\n" +
                    ") AS u2\n" +
                    "ORDER BY rank",
            countQuery = "SELECT COUNT(*) FROM url",
            nativeQuery = true
    )
    Page<RankedUrl> findAllWithRank(Pageable pageable);

    @Query(
            value = "SELECT *\n" +
                    "FROM (\n" +
                    "    SELECT " +
                    "       ROWNUM() AS rank, " +
                    "       u2.* \n" +
                    "    FROM (\n" +
                    "        SELECT u1.*\n" +
                    "        FROM url AS u1\n" +
                    "        ORDER BY redirects DESC\n" +
                    "    ) AS u2\n" +
                    ") AS u3\n" +
                    "WHERE u3.id = ?#{ #id }",
            countQuery = "SELECT COUNT(*) FROM url WHERE id = ?#{ id }",
            nativeQuery = true
    )
    Optional<RankedUrl> findByIdWithRank(Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE url AS u SET u.redirects = u.redirects + 1 WHERE u.id = ?1", nativeQuery = true)
    void increaseRedirectsCount(Integer id);

}
