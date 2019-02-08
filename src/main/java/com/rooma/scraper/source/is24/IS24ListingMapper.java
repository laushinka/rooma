package com.rooma.scraper.source.is24;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IS24ListingMapper {
    public Listing buildDto(Element result) throws ParseException {
        return Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title(getTitle(result))
                .district(getDistrict(result))
                .address(getAddress(result))
                .postcode("")
                .size(getSize(result))
                .price(getPrice(result))
                .numberOfRooms(getNumberOfRooms(result))
                .url(getUrl(result))
                .imageUrl("")
                .source(IS24.NAME)
                .isAvailable(true)
                .build();
    }

    private Float getNumberOfRooms(Element result) throws ParseException {
        String element = result.getElementsByClass("result-list-entry__criteria").text();
        String roomNumber = StringUtils.substringBetween(element, "Wohnfläche", "Zi");
        if (roomNumber != null) {
            String formatted = convertToNonGermanNumberFormat(roomNumber.trim());
            return Float.valueOf(formatted);
        }
        return 1f;
    }

    private String getUrl(Element result) {
        String text = result.getElementsByAttribute("data-go-to-expose-id").attr("data-go-to-expose-id");
        if (text != null) {
            return "https://www.immobilienscout24.de/expose/" + text;
        }
        return "";
    }

    private String getTitle(Element result) {
        return result.getElementsByClass("result-list-entry__brand-title").text();
    }

private Float getSize(Element result) throws ParseException {
    String element = result.getElementsByClass("result-list-entry__criteria").text();
    String size = StringUtils.substringBetween(element, "Kaltmiete ", " m");
    if (size != null) {
        String formatted = convertToNonGermanNumberFormat(size);
        return Float.valueOf(formatted);
    }
    return 0f;
}

    private Float getPrice(Element result) throws ParseException {
        String element = result.getElementsByClass("result-list-entry__criteria").text();
        String price = StringUtils.substringBefore(element, "€").trim();
        if (!price.equals("")) {
            float v = NumberFormat.getInstance(Locale.GERMAN).parse(price).floatValue();
            if (v < 1000) {
                String formatted = NumberFormat.getInstance(new Locale("us")).format(v);
                return Float.valueOf(formatted);
            } else if (v >= 1000){
                String formatted = NumberFormat.getInstance(new Locale("us")).format(v);
                String s = formatted.replaceFirst(",", "");
                return Float.valueOf(s);
            }
        }
        return 0f;
    }

    private String getAddress(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        return text.split(",")[0];
    }

    private String getDistrict(Element result) {
        String text = result.getElementsByClass("result-list-entry__address").text();
        if (text.length() > 0 && text.split(",").length > 2) {
            String districtText = text.split(",")[1];
            if (districtText.length() > 1 && districtText.split("\\(").length > 1) {
                return districtText.split("\\(")[0].trim();
            }
        } else {
            String[] splitDistrict = text.split("\\(");
            return splitDistrict[0].trim();
        }
        return "";
    }

    private String convertToNonGermanNumberFormat(String size) throws ParseException {
        float v = NumberFormat.getInstance(Locale.GERMAN).parse(size).floatValue();
        return NumberFormat.getInstance(new Locale("us")).format(v);
    }
}
