package com.rooma.scraper;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface ListingRepository extends Repository<ListingDTO, Long> {
    ListingDTO save(ListingDTO listing);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ListingDTO")
    void deleteAllBy(SourceName sourceName);

    @Query(value = "SELECT title FROM ListingDTO WHERE price <= 800")
    List<ListingDTO> findByPrice();
}
