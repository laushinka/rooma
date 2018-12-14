package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CraigslistToListingMapperTest {
    private CraigslistToListingMapper listingMapper = new CraigslistToListingMapper();

    @Test
    public void mapsTitle() {
        Element element = Jsoup.parse("<a href=https://berlin.craigslist.de/apa/d/your-own-apartment-in-berlin/6761964279.html data-id=6761964279 class=result-title hdrlnk>*Your own Apartment in Berlin-Fhain, Fast WLAN, longterm rent*NO SCAM!</a>");

        ListingDTO listingDTO = listingMapper.buildDto(element);

        assertThat(listingDTO.getTitle(), is("*Your own Apartment in Berlin-Fhain, Fast WLAN, longterm rent*NO SCAM!"));
    }    
    
    @Test
    public void mapsNumberOfRooms() {
        Element element = Jsoup.parse("<span class=housing> 2br - 52m<sup>2</sup> - </span>");

        ListingDTO listingDTO = listingMapper.buildDto(element);

        assertThat(listingDTO.getNumberOfRooms(), is(2.0F));
    }

    @Test
    public void mapsPrice() {
        Element element = Jsoup.parse("<span class=result-price>€1100</span>");

        ListingDTO listingDTO = listingMapper.buildDto(element);

        assertThat(listingDTO.getPrice(), is(1100.0F));
    }

    @Test
    public void mapsAddress() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain)</span>");

        ListingDTO listingDTO = listingMapper.buildDto(element);

        assertThat(listingDTO.getAddress(), is("Friedrichshain"));
    }
}