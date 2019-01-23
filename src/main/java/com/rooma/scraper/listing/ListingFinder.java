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

import java.util.List;

@Component
@AllArgsConstructor
public class ListingFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingFinder.class);
    private ListingRepository listingRepository;
    private SearchFilterRepository searchFilterRepository;

    @Scheduled(initialDelay = 3000, fixedDelay = 120000)
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
            List<Listing> newResults = getFilterResults(filter);
            if (newResults != null && newResults.size() > 0) {
                processFilters();
                processed ++;
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
                filter.getMinSize());
    }

    private void processFilters() throws UnirestException {
        Unirest.post("https://slack.com/api/chat.postMessage")
                .header("accept", "application/json")
                .body("{\n" +
                        "    \"ok\": true,\n" +
                        "    \"channel\": \"DF1NHMYJD\",\n" +
                        "    \"ts\": \"1548163280.000100\",\n" +
                        "    \"message\": {\n" +
                        "        \"bot_id\": \"BFJMYMT25\",\n" +
                        "        \"type\": \"message\",\n" +
                        "        \"text\": \"Another try\",\n" +
                        "        \"user\": \"UF25WAA8L\",\n" +
                        "        \"ts\": \"1548163280.000100\"\n" +
                        "    }\n" +
                        "}")
                .asJson();
    }
}
