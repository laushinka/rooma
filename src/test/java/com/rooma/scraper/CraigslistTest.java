package com.rooma.scraper;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CraigslistTest {
    private Craigslist craigslist = new Craigslist();

    // Needs to be improved so that it doesn't fetch the real URL
    @Test
    public void testing() {
        List<ListingDTO> listingDTOS = craigslist.fetch("https://berlin.craigslist.de/search/apa?lang=en&cc=gb");

        assertThat(listingDTOS.get(2).getAddress(), is("Prenzlauer berg, Berlin"));
        assertThat(listingDTOS.get(2).getPrice(), is(BigDecimal.valueOf(400)));
        assertThat(listingDTOS.get(2).getSize(), is(BigDecimal.valueOf(0)));
        assertThat(listingDTOS.get(2).getNumberOfRooms(), is(""));
        assertThat(listingDTOS.get(2).getUrl(), is("https://berlin.craigslist.de/apa/d/beautiful-studio-apartment/6757714559.html?lang=en&cc=gb"));
    }
}