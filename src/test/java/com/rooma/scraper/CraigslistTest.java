package com.rooma.scraper;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CraigslistTest {
    private Craigslist craigslist = new Craigslist();

    @Test
    public void testing() {
        List<ListingDTO> listingDTOS = craigslist.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");

        assertThat(listingDTOS.get(0).getPrice(), is(BigDecimal.valueOf(590)));
        assertThat(listingDTOS.get(0).getSize(), is(BigDecimal.valueOf(60)));
        assertThat(listingDTOS.get(0).getNumberOfRooms(), is("2"));
    }
}