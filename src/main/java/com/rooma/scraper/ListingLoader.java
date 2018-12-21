package com.rooma.scraper;

import com.rooma.scraper.Criteria.CriteriaFilter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListingLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListingLoader.class);
    private ListingRepository listingRepository;
    private CriteriaRepository criteriaRepository;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000000)
    void loadListingsJob() {
        List<Listing> results = null;
        CriteriaFilter filter = processFilters();
        try {
            results = getFilterResults(filter);
            LOGGER.info("Found listings {}", results.size());
        } catch (Exception e) {
            LOGGER.info("Could not load listings {}", e.getMessage());
        }

        if (results == null) {
            LOGGER.info("No listings found");
        }
    }

    private List<Listing> getFilterResults(CriteriaFilter filter) {
        return listingRepository.findBy(
                filter.getMaxPrice(),
                filter.getDistrict(),
                filter.getMinNumberOfRooms(),
                filter.getMinSize());
    }

    private CriteriaFilter processFilters() {
        CriteriaFilter filter = getFilter();
        criteriaRepository.save(filter);
        LOGGER.info("Saved filter {}");
        return filter;
    }

    private CriteriaFilter getFilter() {
        return CriteriaFilter.builder()
                .district("mitte")
                .minNumberOfRooms(2f)
                .maxPrice(800f)
                .minSize(40f)
                .build();
    }
}
