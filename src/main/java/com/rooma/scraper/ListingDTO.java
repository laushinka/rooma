package com.rooma.scraper;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ListingDTO {
    private String title;
    private String district;
    private Integer minimumArea;
    private Integer maximumArea;
    private Double minimumPrice;
    private Double maximumPrice;
    private Integer minimumNumberOfRooms;
    private String url;
    private String imageUrl;
}
