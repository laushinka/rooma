package com.rooma.scraper;

public class SourceFactory {
    public static Source create(String name) {
        if (name.contains("craigslist")) {
            return new Craigslist();
        }
        throw new RuntimeException("unknown source");
    }
}
