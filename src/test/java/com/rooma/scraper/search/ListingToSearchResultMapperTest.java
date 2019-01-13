package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListingToSearchResultMapperTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private ListingToSearchResultMapper listingMapper = new ListingToSearchResultMapper();

    @Test
    public void mapsToSlackResponse() throws JsonProcessingException {
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
                .url("Some url")
                .imageUrl("Some image url")
                .isAvailable(true)
                .build();

        SearchResult result = listingMapper.map(listing);

        assertThat(objectMapper.writeValueAsString(result),
                is("{\"fallback\":\"\",\"title\":\"Some title\",\"title_link\":\"Some url\",\"fields\":[{\"title\":\"Address\",\"value\":\"Some address\",\"short\":false},{\"title\":\"Size\",\"value\":\"55.0\",\"short\":true},{\"title\":\"Price\",\"value\":\"700.0\",\"short\":true},{\"title\":\"Number of Rooms\",\"value\":\"2.0\",\"short\":true},{\"title\":\"Source\",\"value\":\"Craigslist\",\"short\":true}]}"));
    }
}