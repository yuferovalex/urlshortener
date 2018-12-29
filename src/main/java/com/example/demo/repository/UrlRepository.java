package com.example.demo.repository;

import com.example.demo.model.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {
    Page<Url> findAllByOrderByRedirectsDesc(Pageable pagination);
}
