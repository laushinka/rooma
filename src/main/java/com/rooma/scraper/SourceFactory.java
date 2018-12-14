package com.rooma.scraper;

public class SourceFactory {
    public static Source create(String name) {
        if (name.contains(SourceName.CRAIGSLIST.name().toLowerCase())) {
            return new Craigslist();
        }
        throw new RuntimeException("unknown source");
    }
}
