package com.rooma.scraper.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AttachmentField {
    private String title;
    private String value;

    @JsonProperty("short")
    private boolean isShort;
}
