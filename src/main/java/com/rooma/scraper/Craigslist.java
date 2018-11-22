package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Craigslist implements Source{
    @Override
    public List<ListingDTO> fetch(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://berlin.craigslist.de/search/apa?lang=en&cc=gb").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements listOfResults = doc.select("li.result-row");
        ListingDTO dto;
        List<ListingDTO> listingDTOList = new ArrayList<>();

        for (Element result : listOfResults) {
            dto = ListingDTO.builder()
                    .title(result.getElementsByClass("result-title").text())
                    .district(result.getElementsByClass("result-hood").text())
                    .area(getArea(result))
                    .price(getPrice(result))
                    .numberOfRooms(getNumberOfRooms(result))
                    .url(result.absUrl("href"))
                    .imageUrl(result.getElementsByAttribute("img").text())
                    .build();

            listingDTOList.add(dto);
        }
        return listingDTOList;
    }

    private String getPrice(Element result) {
        String price = result.getElementsByClass("result-price").text();
        if (!price.equals("")) {
            return price.split("€")[1];
        }
        return "";
    }

    private String getNumberOfRooms(Element result) {
        String room = result.getElementsByClass("housing").text();
        if (!room.equals("")) {
            String trimSpaces = room.replaceAll("\\s", "");
            if (trimSpaces.split("br-").length > 0) {
                return room.split("br-")[0];
            }
        }
        return "";
    }

    private String getArea(Element result) {
        String area = result.getElementsByClass("housing").text();
        String areaSplit;

        if (!area.equals("")) {
            String trimSpaces = area.replaceAll("\\s", "");
            if (trimSpaces.split("br-").length > 0 && trimSpaces.split("br-").length > 1) {
                areaSplit = trimSpaces.split("br-")[1];
            } else {
                areaSplit = trimSpaces.split("br-")[0];
            }
            return areaSplit.trim().split("m")[0];
        }
        return "";
    }
}
