package com.rooma.scraper.source.is24;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.jsoup.nodes.Element;

public class IS24ListingMapper {
    public Listing buildDto(Element result) {
        return Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title(result.getElementsByClass("result-list-entry__brand-title").text())
                .district("")
                .address(result.getElementsByClass("result-list-entry__address").text())
                .postcode("")
                .size(0f)
                .price(0f)
                .numberOfRooms(0f)
                .url("")
                .imageUrl("")
                .source(IS24.NAME)
                .isAvailable(true)
                .build();
    }
}
