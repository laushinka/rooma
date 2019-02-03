package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

public class SlackMarkdownListResponse {
    private List<Listing> listings;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ListingToSearchResultMapper listingMapper = new ListingToSearchResultMapper();

//    TODO
//    public static SlackMarkdownListResponse createFrom(List<Listing> listing) {
//
//    }
    public SlackMarkdownListResponse() {
        this.listings = new ArrayList<>();
    }

    public void add(Listing listing) {
        this.listings.add(listing);
    }

    public boolean isEmpty() {
        return this.listings.size() == 0;
    }

    public String toString(String prompt) throws JsonProcessingException {
        List<SearchResult> searchResultList = new ArrayList<>();

        for (Listing listing : this.listings) {
            SearchResult searchResult = listingMapper.map(listing);
            searchResultList.add(searchResult);
        }

        return String.valueOf(process(searchResultList, prompt));
    }

    private StringBuilder process(List<SearchResult> searchResultList, String prompt) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();

        String stringOfResults = objectMapper.writeValueAsString(searchResultList);

        stringBuilder.append(stringOfResults);

        if (stringBuilder.length() > 0) {
            // to remove the last "]"
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        if (prompt.length() > 0) {
            stringBuilder.append(",");
            stringBuilder.append(prompt);
        }
        stringBuilder.append("]");

        return stringBuilder;
    }
}
