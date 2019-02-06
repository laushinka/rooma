package com.rooma.scraper.search;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class SlackAction {
    private String name;
    private String text;
    private String type;
    private String value;
}
