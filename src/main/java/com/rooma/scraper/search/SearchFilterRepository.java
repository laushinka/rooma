package com.rooma.scraper.search;

import org.springframework.data.repository.Repository;

public interface SearchFilterRepository extends Repository<SearchFilter, Long>{
    SearchFilter save(SearchFilter filter);
}
