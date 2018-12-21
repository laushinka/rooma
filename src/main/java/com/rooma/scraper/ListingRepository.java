package com.rooma.scraper;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface ListingRepository extends Repository<Listing, Long> {
    Listing save(Listing listing);

    @Transactional
    @Modifying
    @Query(value = "DELETE from Listing")
    void deleteAllBy(SourceName sourceName);

    @Query(value = "SELECT title FROM Listing WHERE price <= 800")
    List<Listing> findByPrice();
}
