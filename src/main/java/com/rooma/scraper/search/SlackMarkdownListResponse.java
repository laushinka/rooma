package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

class SlackMarkdownListResponse {
    private static final String PROMPT_QUESTION_ID = "prompt_question_id";
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

    String toJson(SearchFilter filter) throws JsonProcessingException {
        List<SearchResult> searchResultList = new ArrayList<>();

        for (Listing listing : this.listings) {
            SearchResult searchResult = listingMapper.map(listing);
            searchResultList.add(searchResult);
        }

        StringBuilder completedString = process(searchResultList, filter);

        return "{\"attachments\":" + completedString + "}";
    }

    private StringBuilder process(List<SearchResult> searchResultList, SearchFilter filter) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();

        String stringOfResults = objectMapper.writeValueAsString(searchResultList);

        stringBuilder.append(stringOfResults);

        String prompt = objectMapper.writeValueAsString(getQuestionPrompt(filter));

        if (stringBuilder.length() > 0) {
            // to remove the last "]"
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        stringBuilder.append(",");
        stringBuilder.append(prompt);
        stringBuilder.append("]");

        return stringBuilder;
    }

    private QuestionPromptToSaveSearch getQuestionPrompt(SearchFilter filter) throws JsonProcessingException {
        return QuestionPromptToSaveSearch.builder()
                    .fallback("")
                    .title("Would you like to save your query?")
                    .text("We can send you notifications when there are new apartments :)")
                    .callback_id(PROMPT_QUESTION_ID)
                    .actions(getActions(filter))
                    .build();
    }

    private List<SlackAction> getActions(SearchFilter filter) throws JsonProcessingException {
        List<SlackAction> slackActions = new ArrayList<>();

        SlackAction saveAction = SlackAction.builder()
                .name("Save")
                .text("Yes")
                .type("button")
                .value(objectMapper.writeValueAsString(filter))
                .build();

        SlackAction doNotSaveAction = SlackAction.builder()
                .name("No save")
                .text("No")
                .type("button")
                .value("Nothing")
                .build();

        slackActions.add(saveAction);
        slackActions.add(doNotSaveAction);

        return slackActions;
    }
}
