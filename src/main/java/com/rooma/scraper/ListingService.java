package com.rooma.scraper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListingService {
    private ListingRepository listingRepository;

    public ListingDTO saveListing(ListingDTO listing) {
        return listingRepository.save(listing);
    }
}
