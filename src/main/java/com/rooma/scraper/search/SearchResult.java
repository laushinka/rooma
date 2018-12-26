package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchResult {
    private List<Listing> result;
}
