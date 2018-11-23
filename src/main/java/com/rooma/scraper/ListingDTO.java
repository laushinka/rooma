package com.rooma.scraper;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ListingDTO {
    private String title;
    private String district;
    private String area;
    private String price;
    private String numberOfRooms;
    private String url;
    private String imageUrl;
}
