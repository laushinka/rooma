package com.rooma.scraper.source;

import com.rooma.scraper.domain.model.Listing;

import java.util.List;

public interface Source {
    List<Listing> fetch(String url);
    String name();
}
