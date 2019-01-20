package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private final ListingRepository listingRepository;
    private final SearchFilterRepository searchFilterRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(
            value = "/district",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<?> slackSearch(@RequestBody String body) throws JsonProcessingException {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = payload.split("&")[0].split("\\+")[0];
        Float price = Float.valueOf(payload.split("&")[0].split("\\+")[1]);
        Float numberOfRooms = Float.valueOf(payload.split("&")[0].split("\\+")[2]);
        Float minSize = Float.valueOf(payload.split("&")[0].split("\\+")[3]);

        LOGGER.info("Searched district = {}, maxPrice = {}, numberOfRooms = {}, minSize = {}, raw = {}", district, price, numberOfRooms, minSize, body);

        List<Listing> searchResult = listingRepository.findBy(price, district, numberOfRooms, minSize);
        SearchFilter searchFilter = SearchFilter.builder()
                .district(district)
                .maxPrice(price)
                .minNumberOfRooms(numberOfRooms)
                .minSize(minSize)
                .build();

        SlackMarkdownListResponse response = new SlackMarkdownListResponse();
        for(Listing listing : searchResult) {
            response.add(listing);
        }

        if (!response.isEmpty()) {
            return ResponseEntity.ok(response.toJson(searchFilter));
        } else {
            return ResponseEntity.ok("No listings are found for your search criteria :cry:");
        }
    }

    @RequestMapping(
            value = "/criteria",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<SearchFilter> slackSaveSearchRequest(@RequestBody String body) throws IOException {
        String decodedResponse = URLDecoder.decode(body, "UTF-8");
        String result = StringUtils.substringBetween(decodedResponse, "\"value\":\"", "\"}],\"callback_id\"");
        String tidiedResult = result.replaceAll("\\\\", "");

        SearchFilter searchFilter = objectMapper.readValue(tidiedResult, SearchFilter.class);

//        TODO: Extract user_name and persist as SearchFilter field
        searchFilterRepository.save(searchFilter);
        logSearchFilter(searchFilter, body);

        return ResponseEntity.ok(searchFilter);
    }

    private void logSearchFilter(SearchFilter searchFilter, String body) {
        LOGGER.info("Saved query -> district {}, maxPrice {}, rooms {}, minSize {}, rawBody {}",
                searchFilter.getDistrict(),
                searchFilter.getMaxPrice(),
                searchFilter.getMinNumberOfRooms(),
                searchFilter.getMinNumberOfRooms(),
                body);
    }
}
