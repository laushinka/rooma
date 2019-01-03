package com.rooma.scraper.search;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchResult {
    private String title;
    private String district;
    private String address;
    private String postcode;
    private Float size;
    private Float price;
    private Float numberOfRooms;
    private String url;
    private String imageUrl;
    private String source;
}
