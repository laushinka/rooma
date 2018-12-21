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

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getTitle(), is("*Your own Apartment in Berlin-Fhain, Fast WLAN, longterm rent*NO SCAM!"));
    }    
    
    @Test
    public void mapsNumberOfRooms() {
        Element element = Jsoup.parse("<span class=housing> 2br - 52m<sup>2</sup> - </span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getNumberOfRooms(), is(2.0F));
    }

    @Test
    public void mapsSize() {
        Element element = Jsoup.parse("<span class=housing> 2br - 52m<sup>2</sup> - </span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getSize(), is(52F));
    }

    @Test
    public void mapsSizeOnly() {
        Element element = Jsoup.parse("<span class=housing> - 52m<sup>2</sup> - </span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getSize(), is(52F));
    }

    @Test
    public void mapsNonExistingSize() {
        Element element = Jsoup.parse("<span class=housing>2br - </span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getSize(), is(0F));
    }

    @Test
    public void mapsPrice() {
        Element element = Jsoup.parse("<span class=result-price>€1100</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getPrice(), is(1100.0F));
    }

    @Test
    public void mapsAddress() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getAddress(), is("Friedrichshain"));
    }

    @Test
    public void mapsDistrictSeparatedWithDash() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain - Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("friedrichshain"));
    }

    @Test
    public void mapsDistrictSeparatedWithComma() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain, Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("friedrichshain"));
    }

    @Test
    public void mapsDistrictSeparatedWithWhitespace() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("friedrichshain"));
    }

    @Test
    public void mapsDistrictSeparatedWithSlash() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain/Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("friedrichshain"));
    }

    @Test
    public void mapsCommonSpellingMistakeForPberg() {
        Element element = Jsoup.parse("<span class=result-hood> (prenzlauerberg/Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("prenzlauer berg"));
    }

    @Test
    public void mapsNonUmlautSpellingForNeukölln() {
        Element element = Jsoup.parse("<span class=result-hood> (neukoelln/Berlin)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("neukölln"));
    }

    @Test
    public void mapsPrenzlauerBergThatHasTwoWords() {
        Element element = Jsoup.parse("<span class=result-hood>Prenzlauer Berg</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getDistrict(), is("prenzlauer berg"));
    }

    @Test
    public void mapsPostcodeInCompleteAddress() {
        Element element = Jsoup.parse("<span class=result-hood> (Friedrichshain, Berlin, 10243)</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getPostcode(), is("10243"));
    }

    @Test
    public void mapsPostcodeOnly() {
        Element element = Jsoup.parse("<span class=result-hood>10243</span>");

        Listing listing = listingMapper.buildDto(element);

        assertThat(listing.getPostcode(), is("10243"));
    }
}