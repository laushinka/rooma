package com.rooma.scraper;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListingLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingLoader.class);
    private ListingRepository listingRepository;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000000)
    void loadListingsJob() {
        List<Listing> results = null;
        try {
            results = listingRepository.findByPrice();
            LOGGER.info("Found listings {}", results.size());
        } catch (Exception e) {
            LOGGER.info("Could not load listings {}", e.getMessage());
        }

        if (results == null) {
            LOGGER.info("No listings found");
        }
    }
}
