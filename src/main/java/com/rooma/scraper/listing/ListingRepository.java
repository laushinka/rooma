package com.rooma.scraper.listing;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ListingRepository extends Repository<Listing, Long> {
    Listing save(Listing listing);

    @Transactional
    @Modifying
    @Query(value = "DELETE from Listing")
    void deleteAll();

    @Query(value = "FROM Listing l WHERE l.price <= :maxPrice AND l.district like :district AND l.numberOfRooms >= :minNumberOfRooms AND l.size >= :minSize")
    List<Listing> findBy(@Param("maxPrice") Float maxPrice,
                         @Param("district") String district,
                         @Param("minNumberOfRooms") Float numberOfRooms,
                         @Param("minSize") Float minSize
    );

    @Query(value = "FROM Listing l WHERE l.price <= :maxPrice AND l.district like :district AND l.numberOfRooms >= :minNumberOfRooms AND l.size >= :minSize AND l.creationDate >= DATE_SUB(NOW(), INTERVAL 12 HOUR)")
    List<Listing> findNewListingsBy(@Param("maxPrice") Float maxPrice,
                         @Param("district") String district,
                         @Param("minNumberOfRooms") Float numberOfRooms,
                         @Param("minSize") Float minSize
    );
}