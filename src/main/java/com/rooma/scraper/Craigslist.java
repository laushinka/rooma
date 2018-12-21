package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Craigslist implements Source {
    private CraigslistToListingMapper listingMapper = new CraigslistToListingMapper();

    @Override
    public List<Listing> fetch(String url) {
        List<String> listOfPages = new ArrayList<>();
        List<Document> listOfDocuments = new ArrayList<>();
        List<Listing> listingList = new ArrayList<>();

        try {
            List<String> urls = getUrls(listOfPages);
            for (String page : urls) {
                fetchDocuments(listOfDocuments, page);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        processDocuments(listOfDocuments, listingList);

        return listingList;
    }

    @Override
    public String name() {
        return SourceName.CRAIGSLIST.name();
    }

    private void processDocuments(List<Document> listOfDocuments, List<Listing> listingList) {
        for (Document doc : listOfDocuments) {
            Elements listOfResults = doc.select("li.result-row");
            for (Element result : listOfResults) {
                Listing listing = listingMapper.buildDto(result);
                listingList.add(listing);
            }
        }
    }

    private List<String> getUrls(List<String> listOfPages) throws IOException {
        String hostName = "https://berlin.craigslist.de";
        String firstPage = "https://berlin.craigslist.de/search/apa?lang=en&cc=gb";
        listOfPages.add(firstPage);

        for (int i = 0; i < 5; i++) {
            Document firstDoc = Jsoup.connect(listOfPages.get(listOfPages.size() - 1)).get();
            String nextPage = firstDoc.select("a.button.next").attr("href");
            if (!nextPage.equals("")) {
                String nextUrl = hostName + nextPage;
                listOfPages.add(nextUrl);
            }
        }

        return listOfPages;
    }

    private void fetchDocuments(List<Document> listOfDocuments, String page) throws IOException {
        Document document = Jsoup.connect(page).get();
        listOfDocuments.add(document);
    }
}
