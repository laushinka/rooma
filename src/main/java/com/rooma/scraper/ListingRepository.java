package com.rooma.scraper;

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
    void deleteAllBy(SourceName sourceName);

    @Query(value = "SELECT title FROM Listing l WHERE l.price <= :maxPrice AND l.district like :district AND l.numberOfRooms >= :minNumberOfRooms AND l.size >= :minSize AND l.source = :source")
    List<Listing> findBy(@Param("maxPrice") Float maxPrice,
                         @Param("district") String district,
                         @Param("minNumberOfRooms") Float numberOfRooms,
                         @Param("minSize") Float minSize,
                         @Param("source") String source
    );
}
