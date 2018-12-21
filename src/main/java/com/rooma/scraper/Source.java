package com.rooma.scraper;

import java.util.List;

public interface Source {
    List<Listing> fetch(String url);
    String name();
}
