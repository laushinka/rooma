package com.rooma.scraper.listing;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
class ListingSearchController {
    private final ListingRepository listingRepository;

    @GetMapping(
            path = "/search/{maxPrice}/{district}/{minNumberOfRooms}/{minSize}",
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<List<Listing>> search(@PathVariable Float maxPrice,
                                         @PathVariable String district,
                                         @PathVariable Float minNumberOfRooms,
                                         @PathVariable Float minSize
    ) {
        List<Listing> result = listingRepository.findBy(maxPrice, district, minNumberOfRooms, minSize);
        return ResponseEntity.ok(result);
    }
}
