package com.rooma.scraper.listing;

import com.rooma.scraper.search.SearchFilter;
import com.rooma.scraper.search.SearchFilterRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListingFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingFinder.class);
    private ListingRepository listingRepository;
    private SearchFilterRepository searchFilterRepository;

    @Scheduled(initialDelay = 3000, cron = "0 */12 * * *")
    void loadListingsJob() {
        List<Listing> results = null;
        SearchFilter filter = processFilters();
        try {
            results = getFilterResults(filter);
            LOGGER.info("Found listings {}", results.size());
        } catch (Exception e) {
            LOGGER.info("Could not load listings {}", e.getMessage());
        }

        if (results == null) {
            LOGGER.info("No listings found");
        }
    }

    private List<Listing> getFilterResults(SearchFilter filter) {
        return listingRepository.findNewListingsBy(
                filter.getMaxPrice(),
                filter.getDistrict(),
                filter.getMinNumberOfRooms(),
                filter.getMinSize());
    }

    // TODO: This dummy flow won't be used anymore. Instead:
    // 1. Go through every searchFilter in the database
    // 2. For every filter, find if there are new listings (maybe newer than the past 12 hours?)
    //      in the database
    // 3. If new ones are found, send to slackUserId
    private SearchFilter processFilters() {
        return getFilter();
    }

    private SearchFilter getFilter() {
        return SearchFilter.builder()
                .district("mitte")
                .minNumberOfRooms(2f)
                .maxPrice(800f)
                .minSize(40f)
                .slackUserId("UF25WAA8L")
                .build();
    }
}
