package com.rooma.scraper.source.is24;

import com.rooma.scraper.listing.Listing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IS24ListingMapperTest {
    private IS24ListingMapper mapper = new IS24ListingMapper();

    @Test
    public void mapsTitle() throws ParseException {
        Element element = Jsoup.parse("<a href=/Suche/controller/exposeNavigation/goToExpose.go?exposeId=108208623&amp;searchUrl=%2FSuche%2FS-T%2FWohnung-Miete%2FBerlin%2FBerlin&amp;referrer=RESULT_LIST_LISTING&amp;searchType=district data-go-to-expose-id=108208623 data-go-to-expose-referrer=RESULT_LIST_LISTING data-go-to-expose-searchtype=district class=result-list-entry__brand-title-container><h5 class=result-list-entry__brand-title font-h6 onlyLarge nine-tenths margin-bottom-none maxtwolinerHeadline>PARAGON Apartments - 4 Zimmer, EBK, Parkett, und Balkon in Prenzlauer Berg</h5></a>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getTitle(), is("PARAGON Apartments - 4 Zimmer, EBK, Parkett, und Balkon in Prenzlauer Berg"));
    }

    @Test
    public void mapsAddress() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=108208623 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Danziger Straße 73, Prenzlauer Berg (Prenzlauer Berg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getAddress(), is("Danziger Straße 73"));
    }

    @Test
    public void mapsDistrictWithTwoWords() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=108208623 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Danziger Straße 73, Prenzlauer Berg (Prenzlauer Berg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getDistrict(), is("Prenzlauer Berg"));
    }

    @Test
    public void mapsDistrictWithOneWord() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=85827703 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Torstraße 131, Charlottenburg (Charlottenburg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getDistrict(), is("Charlottenburg"));
    }

    @Test
    public void mapsDistrictWithHiddenAddress() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__address><button title=Auf der Karte anzeigen data-result-id=85827703 class=button-link link-internal result-list-entry__map-link><div class=font-ellipsis>Charlottenburg (Charlottenburg), Berlin</div></button></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getDistrict(), is("Charlottenburg"));
    }

    @Test
    public void mapsSizeAndPriceAndNumberOfRooms() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__criteria margin-bottom-s><div><div class=grid grid-flex gutter-horizontal-l gutter-vertical-s data-is24-qa=attributes><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs>1.024,35 €</dd><dt class=font-s onlyLarge>Kaltmiete</dt></dl><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs>42,96 m²</dd><dt class=font-s onlyLarge>Wohnfläche</dt></dl><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs><span><span class=onlySmall>1,5<!-- --> Zi.</span><span class=onlyLarge>1,5</span></span></dd><dt class=font-s onlyLarge><abbr title=Zimmer>Zi.</abbr></dt></dl></div><div class=result-list-entry__secondary-criteria-container font-s margin-top-s ><ul class=result-list-entry__secondary-criteria role=presentation><li class=margin-top-none margin-bottom-xs>Einbauküche</li><li class=margin-top-none margin-bottom-xs>Aufzug</li></ul></div></div></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getSize(), is(42.96F));
        assertThat(listing.getPrice(), is(1024.35F));
        assertThat(listing.getNumberOfRooms(), is(1.5F));
    }

    @Test
    public void mapsSizeAndPriceWithWeirdNumber() throws ParseException {
        Element element = Jsoup.parse("<div class=result-list-entry__criteria margin-bottom-s><div><div class=grid grid-flex gutter-horizontal-l gutter-vertical-s data-is24-qa=attributes><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs>9.999,999 €</dd><dt class=font-s onlyLarge>Kaltmiete</dt></dl><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs>42,96 m²</dd><dt class=font-s onlyLarge>Wohnfläche</dt></dl><dl class=grid-item result-list-entry__primary-criterion  role=presentation><dd class=font-nowrap font-line-xs><span><span class=onlySmall>1<!-- --> Zi.</span><span class=onlyLarge>1</span></span></dd><dt class=font-s onlyLarge><abbr title=Zimmer>Zi.</abbr></dt></dl></div><div class=result-list-entry__secondary-criteria-container font-s margin-top-s ><ul class=result-list-entry__secondary-criteria role=presentation><li class=margin-top-none margin-bottom-xs>Einbauküche</li><li class=margin-top-none margin-bottom-xs>Aufzug</li></ul></div></div></div>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getPrice(), is(9999.999F));
    }

    @Test
    public void mapsUrl() throws ParseException {
        Element element = Jsoup.parse("<a href=/Suche/controller/exposeNavigation/goToExpose.go?exposeId=109394298&amp;searchUrl=%2FSuche%2FS-T%2FP-2%2FWohnung-Miete%2FUmkreissuche%2FBerlin_2dMitte_20_28Mitte_29%2F-%2F228105%2F2512493%2F-%2F1276003001046%2F3%2F1%2C00-%2F38%2C00-%2FEURO--730%2C00&amp;referrer=RESULT_LIST_LISTING&amp;searchType=radius data-go-to-expose-id=109394298 data-go-to-expose-referrer=RESULT_LIST_LISTING data-go-to-expose-searchtype=radius class=result-list-entry__brand-title-container><h5 class=result-list-entry__brand-title font-h6 onlyLarge nine-tenths font-ellipsis><span class=result-list-entry__new-flag margin-right-xs>NEU</span>Geräumige, ruhige 1-Zimmer Wohnung in Berlin-Friedrichshain !</h5></a>");

        Listing listing = mapper.buildDto(element);

        assertThat(listing.getUrl(), is("https://www.immobilienscout24.de/expose/109394298"));
    }
}