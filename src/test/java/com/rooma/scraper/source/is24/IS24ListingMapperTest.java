package com.rooma.scraper.source.is24;

import com.rooma.scraper.listing.Listing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IS24ListingMapperTest {
    private IS24ListingMapper mapper = new IS24ListingMapper();

    @Test
    public void mapsTitle() {
        Element element = Jsoup.parse("<a href=/Suche/controller/exposeNavigation/goToExpose.go?exposeId=108208623&amp;searchUrl=%2FSuche%2FS-T%2FWohnung-Miete%2FBerlin%2FBerlin&amp;referrer=RESULT_LIST_LISTING&amp;searchType=district data-go-to-expose-id=108208623 data-go-to-expose-referrer=RESULT_LIST_LISTING data-go-to-expose-searchtype=district class=result-list-entry__brand-title-container><h5 class=result-list-entry__brand-title font-h6 onlyLarge nine-tenths margin-bottom-none maxtwolinerHeadline>PARAGON Apartments - 4 Zimmer, EBK, Parkett, und Balkon in Prenzlauer Berg</h5></a>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getTitle(), is("PARAGON Apartments - 4 Zimmer, EBK, Parkett, und Balkon in Prenzlauer Berg"));
    }
    
    @Test
    public void mapsAddress() {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=108208623 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Danziger Straße 73, Prenzlauer Berg (Prenzlauer Berg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getAddress(), is("Danziger Straße 73"));
    }

    @Test
    public void mapsDistrict() {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=108208623 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Danziger Straße 73, Prenzlauer Berg (Prenzlauer Berg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getDistrict(), is("Prenzlauer Berg"));
    }
}