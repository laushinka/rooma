package com.rooma.scraper.domain.service;

import com.rooma.scraper.domain.model.Listing;
import com.rooma.scraper.domain.model.ListingRepository;
import com.rooma.scraper.source.Source;
import com.rooma.scraper.source.SourceFactory;
import com.rooma.scraper.source.craigslist.Craigslist;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
class ListingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingService.class);
    private ListingRepository listingRepository;

    @Scheduled(initialDelay = 2000, fixedDelay = 10000)
    void fetchListingsJob() {
        List<Listing> craigslistListings = startFetching();
        deleteAllRows();

        for (Listing listing : craigslistListings) {
            saveNewListings(listing);
        }
    }

    private void saveNewListings(Listing listing) {
        try {
            listingRepository.save(listing);
            LOGGER.info("Saved listing {}", listing.getTitle());
        } catch (Exception e) {
            LOGGER.error("Unable to process {} due to {}", listing.getTitle(), e.getMessage());
        }
    }

    private List<Listing> startFetching() {
        Source craigslistJob = SourceFactory.create("craigslist");
        LOGGER.info("Starting craigslist scheduled job");
        return craigslistJob.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");
    }

    private void deleteAllRows() {
        listingRepository.deleteAllBy(Craigslist.NAME);
        LOGGER.info("Deleted all rows in Craigslist table");
    }
}
