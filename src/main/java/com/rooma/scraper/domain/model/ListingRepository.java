package com.rooma.scraper.domain.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;

public interface ListingRepository extends Repository<Listing, Long> {
    Listing save(Listing listing);

    @Transactional
    @Modifying
    @Query(value = "DELETE from Listing")
    void deleteAllBy(String sourceName);
}
