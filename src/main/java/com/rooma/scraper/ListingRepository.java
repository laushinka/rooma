package com.rooma.scraper;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ListingRepository extends Repository<ListingDTO, Long> {
    ListingDTO save(ListingDTO listing);

    void delete(Long id);

    Optional<ListingDTO> findById(Long id);
}
