package com.rooma.scraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ScraperApplication {
	private ListingRepository listingRepository;

	public static void main(String[] args) {
		SpringApplication.run(ScraperApplication.class, args);

		if (args.length != 1) {
			log("no args");
			System.exit(1);
		}

		Source src = SourceFactory.create(args[0]);
		List<ListingDTO> result = src.fetch("");
		for (ListingDTO listing: result) {
			try {
				listingRepository.save(listing);
				log("%s", listing.getTitle());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void log(String message, String... values) {
		System.out.println(String.format(message, values));
	}
}