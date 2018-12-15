package com.rooma.scraper;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
class ListingService {
    private ListingRepository listingRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingService.class);

    @Scheduled(initialDelay = 2000, fixedDelay = 1000)
    void fetchListingsJob() {
        Source craigslistJob = SourceFactory.create("craigslist");
        LOGGER.info("Starting craigslist scheduled job");
        List<ListingDTO> craigslistListings = craigslistJob.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");
        for (ListingDTO listing : craigslistListings) {
            try {
                listingRepository.save(listing);
                LOGGER.info("Saved listing {}", listing.getTitle());
            } catch (Exception e) {
                LOGGER.error("Unable to save {} due to {}", listing.getTitle(), e.getMessage());
            }
        }
    }
}
