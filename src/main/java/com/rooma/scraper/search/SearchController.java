package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
class SearchController {
    private final ListingRepository listingRepository;
    private final SearchFilterRepository searchFilterRepository;

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

    @RequestMapping(
            value = "/district/{district}",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<SearchFilter> saveSearch(@PathVariable String district,
                                            @RequestParam Float maxPrice,
                                            @RequestParam Float minNumberOfRooms,
                                            @RequestParam Float minSize) {

        SearchFilter filter = SearchFilter.builder()
                .maxPrice(maxPrice)
                .district(district)
                .minNumberOfRooms(minNumberOfRooms)
                .minSize(minSize)
                .build();

        searchFilterRepository.save(filter);
        return ResponseEntity.ok(filter);
    }
}
