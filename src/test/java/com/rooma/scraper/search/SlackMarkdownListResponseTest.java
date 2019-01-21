package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SlackMarkdownListResponseTest {
    private SlackMarkdownListResponse slackMarkdownListResponse = new SlackMarkdownListResponse();

    @Test
    public void convertsToExpectedFormat() throws JsonProcessingException {
        Listing listing = Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title("Some title")
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
                .title("Other title")
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

        slackMarkdownListResponse.add(listing);
        slackMarkdownListResponse.add(listing2);

        String expectedResponse = slackMarkdownListResponse.toJson("");

        assertThat(expectedResponse, is(getSlackTestData()));
    }

    private String getSlackTestData() {
        return "{\"attachments\":[{\"fallback\":\"\",\"title\":\"Some title\",\"title_link\":null,\"fields\":[{\"title\":\"Address\",\"value\":\"Some address\",\"short\":false},{\"title\":\"Size\",\"value\":\"55.0\",\"short\":true},{\"title\":\"Price\",\"value\":\"700.0\",\"short\":true},{\"title\":\"Number of Rooms\",\"value\":\"2.0\",\"short\":true},{\"title\":\"Source\",\"value\":\"Craigslist\",\"short\":true}]},{\"fallback\":\"\",\"title\":\"Other title\",\"title_link\":null,\"fields\":[{\"title\":\"Address\",\"value\":\"Other address\",\"short\":false},{\"title\":\"Size\",\"value\":\"55.0\",\"short\":true},{\"title\":\"Price\",\"value\":\"700.0\",\"short\":true},{\"title\":\"Number of Rooms\",\"value\":\"2.0\",\"short\":true},{\"title\":\"Source\",\"value\":\"Craigslist\",\"short\":true}]}]}";
    }
}
