package com.rooma.scraper.search;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
class SearchResult {
    private String fallback;
    private String title;
    private String title_link;
    private List<AttachmentField> fields;
}