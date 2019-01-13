package com.rooma.scraper.search;

import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SlackMarkdownListResponseTest {
    private SlackMarkdownListResponse slackMarkdownListResponse = new SlackMarkdownListResponse();

    @Test
    public void convertsToExpectedFormat() {
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

        String expectedResponse = slackMarkdownListResponse.toJson();

        assertThat(expectedResponse, is(getSlackTestData()));
    }

    private String getSlackTestData() {
        return "{\"attachments\": [{\n  \"fallback\": \"\",\n  \"title\": \"Some title\",\n  \"title_link\": \"null\",\n  \"fields\": [\n    {\n      \"title\": \"Address\",\n      \"value\": \"Some address\",\n      \"short\": false\n    },\n    {\n      \"title\": \"Size\",\n      \"value\": \"55.00\",\n      \"short\": true\n    },\n    {\n      \"title\": \"Price\",\n      \"value\": \"700.00\",\n      \"short\": true\n    },\n    {\n      \"title\": \"Number of rooms\",\n      \"value\": \"2.00\",\n      \"short\": true\n    }\n  ]\n},{\n  \"fallback\": \"\",\n  \"title\": \"Other title\",\n  \"title_link\": \"null\",\n  \"fields\": [\n    {\n      \"title\": \"Address\",\n      \"value\": \"Other address\",\n      \"short\": false\n    },\n    {\n      \"title\": \"Size\",\n      \"value\": \"55.00\",\n      \"short\": true\n    },\n    {\n      \"title\": \"Price\",\n      \"value\": \"700.00\",\n      \"short\": true\n    },\n    {\n      \"title\": \"Number of rooms\",\n      \"value\": \"2.00\",\n      \"short\": true\n    }\n  ]\n}] }";
    }
}
