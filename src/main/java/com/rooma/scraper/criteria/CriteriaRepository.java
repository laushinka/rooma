package com.rooma.scraper.criteria;

import org.springframework.data.repository.Repository;

public interface CriteriaRepository extends Repository<CriteriaFilter, Long>{
    CriteriaFilter save(CriteriaFilter filter);
}
