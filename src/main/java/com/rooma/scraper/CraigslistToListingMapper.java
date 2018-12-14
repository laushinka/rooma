package com.rooma.scraper;

import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Optional;

import static com.rooma.scraper.BerlinDistricts.convertCommonMisspelling;

public class CraigslistToListingMapper {
    public ListingDTO buildDto(Element result) {
        return ListingDTO.builder()
                .rentalType(RentalType.APARTMENT)
                .title(result.getElementsByClass("result-title").text())
                .district(getDistrict(result))
                .address(getAddress(result))
                .postcode(getPostcode(result))
                .size(getSize(result))
                .price(getPrice(result))
                .numberOfRooms(getNumberOfRooms(result))
                .url(result.getElementsByClass("result-title hdrlnk").attr("href"))
                .imageUrl(result.getElementsByAttribute("img").text())
                .source(SourceName.CRAIGSLIST)
                .isAvailable(true)
                .build();
    }

    private String getPostcode(Element result) {
        String completeAddress = getAddress(result);
        String[] splitAddress = completeAddress.split("\\W");
        if (splitAddress.length > 0) {
            for (String element : splitAddress) {
                if (element.matches("\\d+") && element.length() == 5) {
                    return element;
                }
            }
        }
        return "";
    }

    private String getDistrict(Element result) {
        String completeAddress = getAddress(result);
        String[] splitAddress = completeAddress.split("\\.|,|-|/|\\s+");
        if (splitAddress.length > 0) {
            for (String district : splitAddress) {
                Optional<String> foundDistrict = Arrays.stream(BerlinDistricts.getBerlinDistricts())
                        .filter(d -> d.toLowerCase().equals(district.trim().toLowerCase()))
                        .findFirst();
                if (foundDistrict.isPresent()) {
                    return convertCommonMisspelling(foundDistrict.get());
                }
            }
        }
        return "";
    }

    private String getAddress(Element result) {
        String text = result.getElementsByClass("result-hood").text();
        return text.replaceAll("[()]", "");
    }

    private Float getSize(Element result) {
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
            return Float.valueOf(sizeString);
        }
        return (float) 0;
    }

    private Float getPrice(Element result) {
        String price = result.getElementsByClass("result-price").text();
        if (!price.equals("")) {
            String priceString = price.split("€")[1].trim();
            return Float.valueOf(priceString);
        }
        return (float) 0;
    }

    private Float getNumberOfRooms(Element result) {
        String room = result.getElementsByClass("housing").text();
        if (!room.equals("")) {
            String trimSpaces = room.replaceAll("\\s", "");
            if (trimSpaces.contains("br") && trimSpaces.split("br-").length > 0) {
                return Float.valueOf(trimSpaces.split("br-")[0]);
            }
        }
        return (float) 0;
    }
}
