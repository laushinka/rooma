package com.rooma.scraper.search;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface SearchFilterRepository extends Repository<SearchFilter, Long>{
    SearchFilter save(SearchFilter filter);

    List<SearchFilter> findAll();
}
