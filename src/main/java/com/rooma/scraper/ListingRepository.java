package com.rooma.scraper;

import org.springframework.data.repository.Repository;

public interface ListingRepository extends Repository<ListingDTO, Long> {
    ListingDTO save(ListingDTO listing);
}
