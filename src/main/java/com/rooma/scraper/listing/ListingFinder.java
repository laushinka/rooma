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
    private int processed;

    @Scheduled(initialDelay = 3000, cron = "0 */12 * * *")
    void loadListingsJob() {
        try {
            checkAgainstAllSavedFilters();
            LOGGER.info("Found listings {}", processed);
        } catch (Exception e) {
            LOGGER.info("Could not load listings {}", e.getMessage());
        }
    }

    private void checkAgainstAllSavedFilters() {
        List<SearchFilter> all = searchFilterRepository.findAll();
        for (SearchFilter filter : all) {
            List<Listing> newResults = getFilterResults(filter);
            if (newResults != null && newResults.size() > 0) {
                processFilters(newResults, filter.getSlackUserId());
                processed ++;
            }
        }
    }

    private List<Listing> getFilterResults(SearchFilter filter) {
        return listingRepository.findNewListingsBy(
                filter.getMaxPrice(),
                filter.getDistrict(),
                filter.getMinNumberOfRooms(),
                filter.getMinSize());
    }

    private void processFilters(List<Listing> newResults, String slackUserId) {
        // send to slackUserId
    }
}
