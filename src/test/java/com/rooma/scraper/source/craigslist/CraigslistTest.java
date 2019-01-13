package com.rooma.scraper.source.craigslist;

import com.rooma.scraper.listing.Listing;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CraigslistTest {
    private Craigslist craigslist = new Craigslist();

    @Test
    public void fetchesAllListings() {
        List<Listing> listings = craigslist.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");

        assertThat(listings.size(), is(281));
    }
}