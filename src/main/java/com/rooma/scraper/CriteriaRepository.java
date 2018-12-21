package com.rooma.scraper;

import com.rooma.scraper.Criteria.CriteriaFilter;
import org.springframework.data.repository.Repository;

public interface CriteriaRepository extends Repository<CriteriaFilter, Long>{
    CriteriaFilter save(CriteriaFilter filter);
}
