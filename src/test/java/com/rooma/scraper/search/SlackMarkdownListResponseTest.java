package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SlackMarkdownListResponseTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private SlackMarkdownListResponse slackMarkdownListResponse = new SlackMarkdownListResponse();

    @Test
    public void convertsToExpectedFormat() throws JsonProcessingException {
        List<Listing> listings = new ArrayList<>();

        Listing listing = Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .address("Some address")
                .district("Some district")
                .postcode("Some postcode")
                .numberOfRooms(2f)
                .size(55f)
                .price(700f)
                .source("Craigslist")
                .imageUrl("Some image url")
                .isAvailable(true)
                .build();

        Listing listing2 = Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .address("Other address")
                .district("Other district")
                .postcode("Other postcode")
                .numberOfRooms(2f)
                .size(55f)
                .price(700f)
                .source("Craigslist")
                .imageUrl("Other image url")
                .isAvailable(true)
                .build();

        listings.add(listing);
        slackMarkdownListResponse.add(listing);
        listings.add(listing2);
        slackMarkdownListResponse.add(listing2);

        String expectedResponse = slackMarkdownListResponse.toJson();

        assertThat(expectedResponse, is(getSlackTestData(listings)));
    }

    private String getSlackTestData(List<Listing> listings) throws JsonProcessingException {
        return objectMapper.writeValueAsString("{attachments: " + listings + "}");
    }
}
