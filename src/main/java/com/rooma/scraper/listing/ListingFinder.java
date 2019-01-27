package com.rooma.scraper.listing;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rooma.scraper.search.SearchFilter;
import com.rooma.scraper.search.SearchFilterRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ListingFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingFinder.class);
    private ListingRepository listingRepository;
    private SearchFilterRepository searchFilterRepository;

    @Scheduled(initialDelay = 5000, fixedDelay = 120000)
    void loadListingsJob() {
        try {
            checkAgainstAllSavedFilters();
            LOGGER.info("Found listings {}");
        } catch (Exception e) {
            LOGGER.info("Could not load listings {}", e.getMessage());
        }
    }

    private void checkAgainstAllSavedFilters() throws UnirestException {
        int processed = 0;
        List<SearchFilter> all = searchFilterRepository.findAll();
        for (SearchFilter filter : all) {
            LOGGER.info("Saved filter {}", filter);
            List<Listing> newResults = getFilterResults(filter);
            if (newResults != null && newResults.size() > 0) {
                processFilters();
                processed ++;
                LOGGER.info("Number of new listings {}", newResults);
                LOGGER.info("Call made to Slack with number of processed {}", processed);
            } else {
                LOGGER.info("No new listings yet");
            }
        }
    }

    private List<Listing> getFilterResults(SearchFilter filter) {
        return listingRepository.findNewListingsBy(
                filter.getMaxPrice(),
                filter.getDistrict(),
                filter.getMinNumberOfRooms(),
                filter.getMinSize(),
                LocalDateTime.now().minusHours(1)
        );
    }

    private void processFilters() throws UnirestException {
        Unirest.post("https://slack.com/api/chat.postMessage")
                .header("accept", "application/json")
                .queryString("token", "xoxp-512569703077-512200350292-528746738721-ad8bd5686dbda6adb3c24e453af061b0")
                .queryString("channel", "UF25WAA8L")
                .queryString("text", "Sent from the application")
                .queryString("pretty", "1")
                .asJson();
    }
}
