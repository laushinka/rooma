package com.rooma.scraper;

import org.jsoup.nodes.Element;

import java.math.BigDecimal;

public class ListingMapper {
    public ListingDTO buildDto(Element result) {
        return ListingDTO.builder()
                .rentalType(RentalType.APARTMENT)
                .title(result.getElementsByClass("result-title").text())
                .district("")
                .address(getAddress(result))
                .postcode("")
                .size(getSize(result))
                .price(getPrice(result))
                .numberOfRooms(getNumberOfRooms(result))
                .url(result.getElementsByClass("result-title hdrlnk").attr("href"))
                .imageUrl(result.getElementsByAttribute("img").text())
                .source(SourceName.CRAIGSLIST)
                .isAvailable(true)
                .build();
    }

    private String getAddress(Element result) {
        String text = result.getElementsByClass("result-hood").text();
        return text.replaceAll("[()]", "");
    }

    private BigDecimal getSize(Element result) {
        String size = result.getElementsByClass("housing").text();
        String sizeSplit;

        if (!size.equals("")) {
            String trimSpaces = size.replaceAll("\\s", "");
            String[] tokens = trimSpaces.split("br-");
            if (tokens.length > 1) {
                sizeSplit = tokens[1];
            } else {
                sizeSplit = tokens[0];
            }
            String sizeString = sizeSplit.trim().split("m")[0];
            return BigDecimal.valueOf(Long.parseLong(sizeString));
        }
        return BigDecimal.valueOf(0);
    }

    private BigDecimal getPrice(Element result) {
        String price = result.getElementsByClass("result-price").text();
        if (!price.equals("")) {
            String priceString = price.split("€")[1].trim();
            return BigDecimal.valueOf(Long.parseLong(priceString));
        }
        return BigDecimal.valueOf(0);
    }

    private BigDecimal getNumberOfRooms(Element result) {
        String room = result.getElementsByClass("housing").text();
        if (!room.equals("")) {
            String trimSpaces = room.replaceAll("\\s", "");
            if (trimSpaces.contains("br") && trimSpaces.split("br-").length > 0) {
                return BigDecimal.valueOf(Long.parseLong(trimSpaces.split("br-")[0]));
            }
        }
        return BigDecimal.valueOf(0);
    }
}
