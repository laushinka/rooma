package com.rooma.scraper.listing;

import com.rooma.scraper.source.SourceFactory;
import com.rooma.scraper.source.SourceService;
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

    @Scheduled(initialDelay = 2000, fixedDelay = 300000)
    void fetchListingsJob() {
        int saved = 0;
        int deleted = 0;
        List<Listing> fetchedFromCraigslist = startFetching();

        List<Listing> fromDatabase = listingRepository.findAll();
        boolean nothingIsInDatabase = fromDatabase.size() == 0;

        // If nothing is in the database, fetch and persist everything
        if (nothingIsInDatabase) {
            for (Listing listing : fetchedFromCraigslist) {
                saveNewListings(listing);
            }
            LOGGER.info("Saving into empty database");
        } else {
            for (Listing listing : fetchedFromCraigslist) {
                String listingUrlFromCraigslist = listing.getUrl();
                boolean listingAlreadyExistsInDatabase = listingRepository.findAllByUrl(listingUrlFromCraigslist) != null;
                // If url from Craigslist does not exist yet in the database, persist it
                if (!listingAlreadyExistsInDatabase) {
                    listingRepository.save(listing);
                    saved++;
                }
            }
            LOGGER.info("Saved {} new listings", saved);
        }

        // If url from database doesn't exist anymore on Craigslist, delete the entry from the database
        if (fromDatabase.size() > 0 && fromDatabase.size() != fetchedFromCraigslist.size()) {
            for (Listing listing : fromDatabase) {
                for (Listing fromCraigslist : fetchedFromCraigslist) {
                    if (!listing.getUrl().equals(fromCraigslist.getUrl())) {
                        listingRepository.deleteBy(listing.getId());
                        deleted++;
                    }
                }
            }
        }
        LOGGER.info("Deleted {} old listings from database", deleted);
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
}
