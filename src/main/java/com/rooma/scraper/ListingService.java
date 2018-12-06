package com.rooma.scraper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class ListingService {
    private ListingRepository listingRepository;

    ListingDTO saveListing(ListingDTO listing) {
        return listingRepository.save(listing);
    }
}
