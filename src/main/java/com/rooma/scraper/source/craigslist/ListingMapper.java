package com.rooma.scraper.source.craigslist;

import com.rooma.scraper.helper.BerlinDistricts;
import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.RentalType;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Optional;

import static com.rooma.scraper.helper.BerlinDistricts.convertCommonMisspelling;

public class ListingMapper {
    public Listing buildDto(Element result) {
        return Listing.builder()
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
                .source(Craigslist.NAME)
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
                Optional<String> foundDistrict = Arrays.stream(BerlinDistricts.getNames())
                        .filter(d -> d.equals(district.trim().toLowerCase()))
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

        if (!size.equals("") && size.contains("m")) {
            String withoutSpaces = size.replaceAll("\\s", "");
            String[] elements = withoutSpaces.split("-");

            for (String element : elements) {
                if (element.contains("m")) {
                    return Float.valueOf(element.split(("m"))[0]);
                }
            }
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
