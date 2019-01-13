package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;

public class ListingToSearchResultMapper {
    public SearchResult map(Listing listing) {
        return SearchResult.builder()
                .title(listing.getTitle())
                .price(listing.getPrice())
                .numberOfRooms(listing.getNumberOfRooms())
                .size(listing.getSize())
                .address(listing.getAddress())
                .district(listing.getDistrict())
                .postcode(listing.getPostcode())
                .url(listing.getUrl())
                .imageUrl(listing.getImageUrl())
                .build();
    }
}
