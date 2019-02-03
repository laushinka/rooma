package com.rooma.scraper.listing;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingRepository extends Repository<Listing, Long> {
    Listing save(Listing listing);

    @Query(value = "DELETE from Listing WHERE ID = :listingId")
    void deleteBy(@Param("listingId") Long listingId);

    @Query(value = "FROM Listing l WHERE l.price <= :maxPrice AND l.district like :district AND l.numberOfRooms >= :minNumberOfRooms AND l.size >= :minSize")
    List<Listing> findBy(@Param("maxPrice") Float maxPrice,
                         @Param("district") String district,
                         @Param("minNumberOfRooms") Float numberOfRooms,
                         @Param("minSize") Float minSize
    );

    @Query(value = "FROM Listing l WHERE l.price <= :maxPrice AND l.district like :district AND l.numberOfRooms >= :minNumberOfRooms AND l.size >= :minSize AND l.creationDate >= :creationDate")
    List<Listing> findNewListingsBy(@Param("maxPrice") Float maxPrice,
                                    @Param("district") String district,
                                    @Param("minNumberOfRooms") Float numberOfRooms,
                                    @Param("minSize") Float minSize,
                                    @Param("creationDate")LocalDateTime creationDate);

    List<Listing> findAll();

    @Query(value = "FROM Listing l WHERE l.url = :url")
    Listing findAllByUrl(@Param("url") String url);
}