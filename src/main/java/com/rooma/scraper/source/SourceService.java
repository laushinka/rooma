package com.rooma.scraper.source;

import com.rooma.scraper.listing.Listing;

import java.util.List;

public interface SourceService {
    List<Listing> fetch(String url);
    String name();
}
