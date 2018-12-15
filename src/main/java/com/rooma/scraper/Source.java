package com.rooma.scraper;

import java.util.List;

public interface Source {
    List<ListingDTO> fetch(String url);
    String name();
}
