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

    @GetMapping(
            path = "/district/{district}?maxPrice={maxPrice}&minNumberOfRooms={minNumberOfRooms}&minSize={minSize}",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<List<Listing>> search(@PathVariable String district,
                                         @RequestParam Float maxPrice,
                                         @RequestParam Float minNumberOfRooms,
                                         @RequestParam Float minSize) {
        List<Listing> result = listingRepository.findBy(maxPrice, district, minNumberOfRooms, minSize);
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            path = "/district/{district}?maxPrice={maxPrice}&minNumberOfRooms={minNumberOfRooms}&minSize={minSize}",
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
