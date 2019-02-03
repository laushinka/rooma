package com.rooma.scraper.source.is24;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class IS24ListingMapper {
    public Listing buildDto(Element result) {
        return Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title(getTitle(result))
                .district(getDistrict(result))
                .address(getAddress(result))
                .postcode("")
                .size(getSize(result))
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

private Float getSize(Element result) {
    String element = result.getElementsByClass("result-list-entry__criteria").text();
    String size = StringUtils.substringBetween(element, "Kaltmiete ", " m");
    if (size != null) {
        return Float.valueOf(size);
    }
    return 0f;
}

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
