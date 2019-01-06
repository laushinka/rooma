package com.rooma.scraper.source.is24;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.jsoup.nodes.Element;

public class IS24ListingMapper {
    public Listing buildDto(Element result) {
        return Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title(result.getElementsByClass("result-list-entry__brand-title").text())
                .district(getDistrict(result))
                .address(getAddress(result))
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

    private String getAddress(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        return text.split(",")[0];
    }

    private String getDistrict(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        String[] districtElements = text.split(",")[1].trim().split("\\(");
        if (districtElements.length > 0) {
            return districtElements[0].trim();
        } else {
            return "";
        }
    }
}
