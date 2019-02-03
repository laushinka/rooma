package com.rooma.scraper.search;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SearchFilterRepository extends Repository<SearchFilter, Long>{
    SearchFilter save(SearchFilter filter);

    List<SearchFilter> findAll();

    @Modifying
    @Transactional
    @Query(value = "DELETE from SearchFilter WHERE ID = :searchFilterId")
    void deleteBy(@Param("searchFilterId") Long searchFilterId);
}
