package com.rooma.scraper.listing;

import com.rooma.scraper.RentalType;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListingRepositoryIT {
    @Autowired
    private ListingRepository listingRepository;

    @Ignore
    @Test
    public void save() {
        Listing listing = Listing.builder()
                .id(34567L)
                .title("Great title")
                .address("Some address")
                .district("Some district")
                .isAvailable(true)
                .imageUrl("Some image url")
                .numberOfRooms(2f)
                .price(550f)
                .size(60f)
                .source("Craigslist")
                .rentalType(RentalType.APARTMENT)
                .creationDate(LocalDateTime.now().minusHours(1))
                .modificationDate(LocalDateTime.now().minusHours(1))
                .build();

        listingRepository.save(listing);
        List<Listing> foundListing = listingRepository.findBy(600f, "Some district", 1f, 50f);

        assertThat(foundListing.size(), is(1));
        assertThat(foundListing.get(0).getTitle(), is("Great title"));
    }
}