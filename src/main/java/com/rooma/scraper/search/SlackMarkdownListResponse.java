package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

class SlackMarkdownListResponse {
    private List<Listing> listings;
    private ObjectMapper mapper = new ObjectMapper();

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
        return mapper.writeValueAsString("{attachments: " + listings + "}");
    }
}
