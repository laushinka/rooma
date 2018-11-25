package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Craigslist implements Source {
    @Override
    public List<ListingDTO> fetch(String url) {
        Document document;
        List<Document> listOfDocuments = new ArrayList<>();
        ListingDTO dto;
        List<ListingDTO> listingDTOList = new ArrayList<>();
        try {
            String[] urls = {"https://berlin.craigslist.de/search/apa?lang=en&cc=gb",
                    "https://berlin.craigslist.de/search/apa?s=120",
                    "https://berlin.craigslist.de/search/apa?s=240",
                    "https://berlin.craigslist.de/search/apa?s=360",
                    "https://berlin.craigslist.de/search/apa?s=480"};
            for (String page : urls) {
                document = Jsoup.connect(page).get();
                listOfDocuments.add(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Document doc : listOfDocuments) {
            Elements listOfResults = doc.select("li.result-row");
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
            String[] tokens = trimSpaces.split("br-");
            if (tokens.length > 1) {
                areaSplit = tokens[1];
            } else {
                areaSplit = tokens[0];
            }
            return areaSplit.trim().split("m")[0];
        }
        return "";
    }
}
