package com.rooma.scraper;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
class ListingDTO {
    private RentalType rentalType;
    private String title;
    private String district;
    private String address;
    private String postcode;
    private Float size;
    private Float price;
    private String numberOfRooms;
    private String url;
    private String imageUrl;
    private String source;
    private boolean isAvailable;
    private Date creationDate;
    private Date modificationDate;
}
