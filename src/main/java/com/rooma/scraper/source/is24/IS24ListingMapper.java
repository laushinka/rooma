package com.rooma.scraper.source.is24;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.jsoup.nodes.Element;

public class IS24ListingMapper {
    public Listing buildDto(Element result) {
        return Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title(getTitle(result))
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

    private String getTitle(Element result) {
        return result.getElementsByClass("result-list-entry__brand-title").text();
    }
//
//    private float getSize(Element result) {
//        Element element = result.getElementsByAttribute("result-list-entry__criteria.margin-bottom-s > div > div.grid.grid-flex.gutter-horizontal-l.gutter-vertical-s > dl:nth-child(2) > dd").get(0);
//        return Float.parseFloat(String.valueOf(element));
//    }

    private String getAddress(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        return text.split(",")[0];
    }

    private String getDistrict(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        if (text.length() > 0) {
            String districtText = text.split(",")[1];
            if (districtText.length() > 1 && districtText.split("\\(").length > 1) {
                return districtText.split("\\(")[0].trim();
            }
        }
        return "";
    }
}
