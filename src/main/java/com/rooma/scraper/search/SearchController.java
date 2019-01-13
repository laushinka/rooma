package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private final ListingRepository listingRepository;
    private final SearchFilterRepository searchFilterRepository;
    private ListingToSearchResultMapper mapper = new ListingToSearchResultMapper();

    @RequestMapping(
            value = "/district",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<?> slackSearch(@RequestBody String body) {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = payload.split("&")[0].split("\\+")[0];
        Float price = Float.valueOf(payload.split("&")[0].split("\\+")[1]);
        Float numberOfRooms = Float.valueOf(payload.split("&")[0].split("\\+")[2]);
        Float minSize = Float.valueOf(payload.split("&")[0].split("\\+")[3]);

        LOGGER.info("Searched district = {}, maxPrice = {}, numberOfRooms = {}, minSize = {}, raw = {}", district, price, numberOfRooms, minSize, body);

        List<Listing> result = listingRepository.findBy(price, district, numberOfRooms, minSize);

        SlackMarkdownListResponse resp = new SlackMarkdownListResponse();
        for(Listing listing : result) {
            resp.add(listing);
        }

        if (!resp.isEmpty()) {
            return ResponseEntity.ok(resp.toJson());
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
    ResponseEntity<SearchFilter> slackSaveSearchRequest(@RequestBody String body) {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = payload.split("&")[0].split("\\+")[0];
        Float maxPrice = Float.valueOf(payload.split("&")[0].split("\\+")[1]);
        Float minNumberOfRooms = Float.valueOf(payload.split("&")[0].split("\\+")[2]);
        Float minSize = Float.valueOf(payload.split("&")[0].split("\\+")[3]);

        SearchFilter filter = SearchFilter.builder()
                .maxPrice(maxPrice)
                .district(district)
                .minNumberOfRooms(minNumberOfRooms)
                .minSize(minSize)
                .build();

        LOGGER.info("Saved district = {}, maxPrice = {}, numberOfRooms = {}, minSize = {}", district, maxPrice, minNumberOfRooms, minSize);

        searchFilterRepository.save(filter);
        return ResponseEntity.ok(filter);
    }

    @RequestMapping(
            value = "/district/{district}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<List<Listing>> search(@PathVariable String district,
                                         @RequestParam(required = false, defaultValue = "100000") Float maxPrice,
                                         @RequestParam(required = false, defaultValue = "1") Float minNumberOfRooms,
                                         @RequestParam(required = false, defaultValue = "1") Float minSize) {
        List<Listing> result = listingRepository.findBy(maxPrice, district, minNumberOfRooms, minSize);
        return ResponseEntity.ok(result);
    }
}
