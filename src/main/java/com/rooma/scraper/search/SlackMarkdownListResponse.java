package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

public class SlackMarkdownListResponse {
    private List<Listing> listings;

    public SlackMarkdownListResponse() {
        this.listings = new ArrayList<>();
    }

    public void add(Listing listing) {
        this.listings.add(listing);
    }

    public boolean isEmpty() {
        return this.listings.size() == 0;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"attachments\": [");
        for (Listing listing : this.listings) {
            sb.append(listing.toJson());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("] }");
        return sb.toString();
    }
}
