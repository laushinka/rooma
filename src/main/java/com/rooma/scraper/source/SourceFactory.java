package com.rooma.scraper.source;

import com.rooma.scraper.source.craigslist.Craigslist;

public class SourceFactory {
    public static Source create(String name) {
        if (name.contains(Craigslist.NAME.toLowerCase())) {
            return new Craigslist();
        }
        throw new RuntimeException("unknown source");
    }
}
