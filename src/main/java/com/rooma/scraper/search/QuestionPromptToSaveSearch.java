package com.rooma.scraper.search;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
class QuestionPromptToSaveSearch {
    private String fallback;
    private String title;
    private String text;
    private String callback_id;
    private List<SlackAction> actions;
}
