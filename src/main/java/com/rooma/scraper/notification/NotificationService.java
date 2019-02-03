package com.rooma.scraper.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import com.rooma.scraper.search.SearchFilter;
import com.rooma.scraper.search.SearchFilterRepository;
import com.rooma.scraper.search.SlackMarkdownListResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private ListingRepository listingRepository;
    private SearchFilterRepository searchFilterRepository;

    @Scheduled(initialDelay = 5000, fixedDelay = 300000)
    void notificationJob() {
        try {
            processSavedFilters();
        } catch (Exception e) {
            LOGGER.info("Exception from possibly too large content {}", e.getMessage());
        }
    }

    private void processSavedFilters() throws UnirestException, JsonProcessingException {
        List<SearchFilter> filtersFound = searchFilterRepository.findAll();
        if (filtersFound.size() > 0) {
            for (SearchFilter filter : filtersFound) {
                List<Listing> newResults = findListingsBasedOn(filter);
                if (newResults != null && newResults.size() > 0) {
                    String attachment = convertToAcceptedFormat(newResults);
                    if (attachment != null) {
                        sendToSlack(attachment, filter.getSlackUserId());
                        LOGGER.info("Sent to Slack {} new results", newResults.size());
                    }
                } else {
                    LOGGER.info("No new listings yet");
                }
            }
        }

    }

    private List<Listing> findListingsBasedOn(SearchFilter filter) {
        return listingRepository.findNewListingsBy(
                filter.getMaxPrice(),
                filter.getDistrict(),
                filter.getMinNumberOfRooms(),
                filter.getMinSize(),
                LocalDateTime.now().minusHours(1)
        );
    }

    private String convertToAcceptedFormat(List<Listing> newResults) throws JsonProcessingException {
        String attachmentValue;
        SlackMarkdownListResponse response = new SlackMarkdownListResponse();
        for(Listing listing : newResults) {
            response.add(listing);
        }
        if (!response.isEmpty()) {
            attachmentValue = response.toString("");
        } else {
            return null;
        }
        return attachmentValue;
    }

    private void sendToSlack(String attachment, String slackUserId) throws UnirestException {
        Unirest.post("https://slack.com/api/chat.postMessage")
                .header("accept", "application/json")
                .queryString("token", "xoxp-512569703077-512200350292-528746738721-ad8bd5686dbda6adb3c24e453af061b0")
                .queryString("channel", slackUserId)
                .queryString("text", "This is what we found for your search:")
                .queryString("attachments", attachment)
                .queryString("pretty", "1")
                .asJson();
    }
}
