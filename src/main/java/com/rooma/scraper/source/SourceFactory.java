package com.rooma.scraper.source;

import com.rooma.scraper.source.craigslist.Craigslist;
import com.rooma.scraper.source.is24.IS24;

public class SourceFactory {
    public static SourceService create(String name) {
        if (name.contains(Craigslist.NAME.toLowerCase())) {
            return new Craigslist();
        } else if (name.contains(IS24.NAME.toLowerCase())) {
            return new IS24();
        }
        throw new RuntimeException("unknown source");
    }
}
