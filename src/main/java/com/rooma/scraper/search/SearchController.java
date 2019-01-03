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

    @RequestMapping(
            value = "/district",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<List<Listing>> slackSearch(@RequestBody String body) {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = StringUtils.substringBefore(payload.split("&")[0], "+");
        String price = StringUtils.substringAfter(payload.split("&")[0], "+");

        LOGGER.info("District = {}, maxPrice = {}", district, price);

        List<Listing> result = listingRepository.findBy(Float.valueOf(price), district, 1f, 30f);
        return ResponseEntity.ok(result);
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
