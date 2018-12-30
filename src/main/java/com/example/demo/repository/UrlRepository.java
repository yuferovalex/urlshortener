package com.example.demo.repository;

import com.example.demo.model.RankedUrl;
import com.example.demo.model.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
    RankedUrl findByIdWithRank(Integer id);

}
