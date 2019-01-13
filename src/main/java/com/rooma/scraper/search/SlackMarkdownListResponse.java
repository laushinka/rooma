package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

class SlackMarkdownListResponse {
    private List<Listing> listings;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ListingToSearchResultMapper listingMapper = new ListingToSearchResultMapper();

    SlackMarkdownListResponse() {
        this.listings = new ArrayList<>();
    }

    void add(Listing listing) {
        this.listings.add(listing);
    }

    boolean isEmpty() {
        return this.listings.size() == 0;
    }

    String toJson() throws JsonProcessingException {
        List<SearchResult> searchResultList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (Listing listing : this.listings) {
            SearchResult searchResult = listingMapper.map(listing);
            searchResultList.add(searchResult);
        }

        String stringOfResults = objectMapper.writeValueAsString(searchResultList);

        stringBuilder.append(stringOfResults);

        String prompt = objectMapper.writeValueAsString(getQuestionPrompt());

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        stringBuilder.append(",");
        stringBuilder.append(prompt);
        stringBuilder.append("]");

        return "{\"attachments\":" + stringBuilder + "}";
    }

    private QuestionPromptToSaveSearch getQuestionPrompt() {
        return QuestionPromptToSaveSearch.builder()
                    .fallback("")
                    .title("Would you like to save your query?")
                    .text("We can send you notifications when there are new apartments :)")
                    .callback_id("some_callback_id")
                    .actions(getActions())
                    .build();
    }

    private List<SlackAction> getActions() {
        List<SlackAction> slackActions = new ArrayList<>();
        SlackAction saveAction = SlackAction.builder()
                .name("Save")
                .text("Yes")
                .type("button")
                .value("Positive")
                .build();

        SlackAction doNotSaveAction = SlackAction.builder()
                .name("No save")
                .text("No")
                .type("button")
                .value("Negative")
                .build();

        slackActions.add(saveAction);
        slackActions.add(doNotSaveAction);

        return slackActions;
    }
}
