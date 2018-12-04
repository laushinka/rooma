package com.rooma.scraper;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Builder
@Getter
class ListingDTO {
    @Id
    @GeneratedValue
    private Long id;

    private RentalType rentalType;
    private String title;
    private String district;
    private String address;
    private String postcode;
    private BigDecimal size;
    private BigDecimal price;
    private String numberOfRooms;
    private String url;
    private String imageUrl;
    private String source;
    private boolean isAvailable;
    private Date creationDate;
    private Date modificationDate;
}
