package com.rooma.scraper.source.is24;

import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.source.SourceService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class IS24 implements SourceService{
    private IS24ListingMapper mapper = new IS24ListingMapper();
    public static final String NAME = "immobilienscout";

    @SuppressWarnings("Duplicates")
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

        try {
            processDocuments(listOfDocuments, listingList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listingList;
    }

    private void processDocuments(List<Document> listOfDocuments, List<Listing> listingList) throws ParseException {
        for (Document doc : listOfDocuments) {
            Elements listOfResults = doc.select(".result-list__listing");
            for (Element result : listOfResults) {
                Listing listing = mapper.buildDto(result);
                listingList.add(listing);
            }
        }
    }

    private void fetchDocuments(List<Document> listOfDocuments, String page) throws IOException {
        Document document = Jsoup.connect(page).get();
        listOfDocuments.add(document);
    }

    private List<String> getUrls(List<String> listOfPages) throws IOException {
        String hostName = "https://www.immobilienscout24.de";
        String firstPage = "https://www.immobilienscout24.de/Suche/S-T/Wohnung-Miete/Berlin/Berlin";
        listOfPages.add(firstPage);

        for (int i = 0; i < 30; i++) {
            Document firstDoc = Jsoup.connect(listOfPages.get(listOfPages.size() - 1)).get();
            String nextPage = firstDoc.select("a:contains(nächste Seite)").attr("href");
            if (!nextPage.equals("")) {
                String nextUrl = hostName + nextPage;
                listOfPages.add(nextUrl);
            }
        }

        return listOfPages;
    }

    @Override
    public String name() {
        return IS24.NAME;
    }
}
