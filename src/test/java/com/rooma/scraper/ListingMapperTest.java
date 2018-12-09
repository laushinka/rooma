package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListingMapperTest {
    private ListingMapper listingMapper = new ListingMapper();

    @Test
    public void mapsCorrectly() {
        Document doc = Jsoup.parse(TestData.getResult());
        Elements listOfElements = doc.select("li.result-row");

        ListingDTO listingDTO = listingMapper.buildDto(listOfElements.first());

        assertThat(listingDTO.getTitle(), is("*Your own Apartment in Berlin-Fhain, Fast WLAN, longterm rent*NO SCAM!"));
        assertThat(listingDTO.getAddress(), is("Friedrichshain"));
        assertThat(listingDTO.getUrl(), is("https://berlin.craigslist.de/apa/d/your-own-apartment-in-berlin/6761964279.html"));
        assertThat(listingDTO.getNumberOfRooms(), is(BigDecimal.valueOf(2)));
        assertThat(listingDTO.getPrice(), is(BigDecimal.valueOf(1100)));
        assertThat(listingDTO.getSize(), is(BigDecimal.valueOf(52)));
    }
}