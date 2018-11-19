package com.rooma.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScraperApplication.class, args);

		if (args.length != 1) {
			log("no args");
			System.exit(1);
		}

		Source src = SourceFactory.create(args[0]);
		String result = src.fetch("");
		log(result);
	}

	private static void log(String message, String... values) {
		System.out.println(String.format(message, values));
	}
}