package com.rooma.scraper.listing;

import com.rooma.scraper.source.SourceService;
import com.rooma.scraper.source.SourceFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
class ListingFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingFetcher.class);
    private ListingRepository listingRepository;

    @Scheduled(initialDelay = 2000, fixedDelay = 1000000000)
    void fetchListingsJob() {
        deleteAllRows();
        List<Listing> craigslistListings = startFetching();

        for (Listing listing : craigslistListings) {
            saveNewListings(listing);
        }
        LOGGER.info("Saved {} listings", craigslistListings.size());
    }

    private void saveNewListings(Listing listing) {
        try {
            listingRepository.save(listing);
        } catch (Exception e) {
            LOGGER.error("Unable to process {} due to {}", listing.getTitle(), e.getMessage());
        }
    }

    private List<Listing> startFetching() {
        SourceService craigslistJob = SourceFactory.create("craigslist");
        LOGGER.info("Starting craigslist scheduled job");
        return craigslistJob.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");
    }

    private void deleteAllRows() {
        listingRepository.deleteAll();
        LOGGER.info("Deleted all rows in craigslist table");
    }
}
