package com.rooma.scraper.listing;

import com.rooma.scraper.source.SourceFactory;
import com.rooma.scraper.source.SourceService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
class ListingFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingFetcher.class);
    private ListingRepository listingRepository;

    @Scheduled(initialDelay = 2000, fixedDelay = 3600000)
    void fetchListingsJob() {
        List<Listing> fetchedListings = startFetching();
        List<Listing> fromDatabase = listingRepository.findAll();

        boolean nothingIsInDatabase = fromDatabase.size() == 0;

        if (nothingIsInDatabase) {
            persistEverything(fetchedListings);
        } else {
            persistNewOnes(fetchedListings);
        }

        if (fromDatabase.size() > 0 && fromDatabase.size() != fetchedListings.size()) {
            removeOldEntries(fetchedListings, fromDatabase);
        }
    }

    private void removeOldEntries(List<Listing> fetchedListings, List<Listing> fromDatabase) {
        int deleted = 0;

        for (Listing listing : fromDatabase) {
            for (Listing fromCraigslist : fetchedListings) {
                if (!listing.getUrl().equals(fromCraigslist.getUrl())) {
                    listingRepository.deleteBy(listing.getId());
                    deleted++;
                }
            }
        }
        LOGGER.info("Deleted {} old listings from database", deleted);
    }

    private void persistNewOnes(List<Listing> fetchedListings) {
        int saved = 0;

        for (Listing listing : fetchedListings) {
            String listingUrlFromCraigslist = listing.getUrl();
            boolean listingAlreadyExistsInDatabase = listingRepository.findAllByUrl(listingUrlFromCraigslist) != null;
            if (!listingAlreadyExistsInDatabase) {
                listingRepository.save(listing);
                saved++;
            }
        }
        LOGGER.info("Saved {} new listings", saved);
    }

    private void persistEverything(List<Listing> fetchedListings) {
        for (Listing listing : fetchedListings) {
            try {
                listingRepository.save(listing);
            } catch (Exception e) {
                LOGGER.error("Unable to process {} due to {}", listing.getTitle(), e.getMessage());
            }
        }
        LOGGER.info("Saving into empty database");
    }

    private List<Listing> startFetching() {
        List<Listing> craigslistListings = fetchFromCraigslist();
        List<Listing> is24Listings = fetchFromIs24();

        Stream<Listing> combinedStream = Stream.of(craigslistListings, is24Listings).flatMap(Collection::stream);

        return combinedStream.collect(Collectors.toList());
    }

    private List<Listing> fetchFromIs24() {
        SourceService is24Job = SourceFactory.create("immobilienscout");
        LOGGER.info("Starting ImmoScout scheduled job");
        return is24Job.fetch("https://www.immobilienscout24.de/Suche/S-T/Wohnung-Miete/Berlin/Berlin");
    }

    private List<Listing> fetchFromCraigslist() {
        SourceService craigslistJob = SourceFactory.create("craigslist");
        LOGGER.info("Starting Craigslist scheduled job");
        return craigslistJob.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");
    }
}
