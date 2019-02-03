package com.rooma.scraper.source.is24;

import com.rooma.scraper.listing.Listing;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IS24Test {
    private IS24 is24 = new IS24();

    @Ignore
    @Test
    public void fetchesAllListings() {
        List<Listing> listings = is24.fetch("https://www.immobilienscout24.de/Suche/S-T/Wohnung-Miete/Berlin/Berlin");

        assertThat(listings.size() > 0, is(true));
    }
}