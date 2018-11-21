package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Craigslist implements Source{
    @Override
    public String fetch(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://berlin.craigslist.de/search/apa?lang=en&cc=gb").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements listOfResults = doc.select("li.result-row");
        StringBuffer sb = new StringBuffer();
        for (Element result : listOfResults) {
            String temp = String.format("%s\t%s\t%s\t%s\t%s\n",
                    result.getElementsByClass("result-title").text(),
                    result.getElementsByClass("result-price").text(),
                    result.getElementsByClass("housing").text(),
                    result.getElementsByClass("result-hood").text());
            sb.append(temp);
        }
        return sb.toString();
    }
}
