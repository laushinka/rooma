package com.rooma.scraper.domain.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
public class Listing {
    @Id
    @GeneratedValue
    private Long id;

    private RentalType rentalType;
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
    private boolean isAvailable;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    @PrePersist
    @PreUpdate
    public void setCreationDateAndModificationDateToNow() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        modificationDate = LocalDateTime.now();
    }
}
