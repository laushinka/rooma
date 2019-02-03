package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequiredArgsConstructor
class SearchController {
    private static final String PROMPT_QUESTION_ID = "prompt_question_id";
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private final ListingRepository listingRepository;
    private final SearchFilterRepository searchFilterRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, SlackMarkdownListResponse> cache = new HashMap<>();

    @RequestMapping(
            value = "/district",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<?> slackSearch(@RequestBody String body) throws JsonProcessingException {
        LOGGER.info("Raw -> {}", body);

        SearchFilter searchFilter = SearchFilter.buildFilterFromSearchRequestPayload(body);

        List<Listing> searchResult = listingRepository.findBy(
                searchFilter.getMaxPrice(),
                searchFilter.getDistrict(),
                searchFilter.getMinNumberOfRooms(),
                searchFilter.getMinSize());

        SlackMarkdownListResponse response = new SlackMarkdownListResponse();

        for(Listing listing : searchResult) {
            response.add(listing);
        }

        if (!response.isEmpty()) {
            this.cache.put(searchFilter.sha256(), response);
            String prompt = objectMapper.writeValueAsString(getQuestionPrompt(searchFilter));
            String completedString = response.toString(prompt);

            return ResponseEntity.ok("{\"attachments\":" + completedString + "}");
        } else {
            return ResponseEntity.ok("No listings are found for your search criteria :cry:");
        }
    }

    @RequestMapping(
            value = "/criteria",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_UTF8_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    ResponseEntity<?> slackSaveSearchRequest(@RequestBody String body) throws IOException, UnirestException {
        String withoutPayloadText = StringUtils.substringAfter(body, "payload=");
        String decodedResponse = URLDecoder.decode(withoutPayloadText, "UTF-8");

        String result = SearchFilter.buildFilterFromSaveRequestPayload(decodedResponse);
        SearchFilter searchFilter = objectMapper.readValue(result, SearchFilter.class);

        convertUnicodeDistrictToUtf8(body, searchFilter);

        SearchFilter a = searchFilterRepository.save(searchFilter);

        logSearchFilter(searchFilter, body);

        SlackMarkdownListResponse response = this.cache.get(a.sha256());

        if (response == null) {
            return ResponseEntity.ok("");
        } else {
            this.cache.remove(searchFilter.toString());
        }

        String completedString = response.toString("");

        String responseUrl = SearchFilter.extractResponseUrl(decodedResponse);

        Unirest.post(responseUrl)
                .body("{\"attachments\":" + completedString + "}")
                .asJson();

        if (SearchFilter.doNotSaveSearchFilter(decodedResponse)) {
            searchFilterRepository.deleteBy(searchFilter.getId());
        }

        return ResponseEntity.ok("");
    }

    private void convertUnicodeDistrictToUtf8(@RequestBody String body, SearchFilter searchFilter) throws UnsupportedEncodingException {
        String district = StringUtils.substringBetween(body, "district%5C%22%3A%5C%22", "%5C%22%2C%5C%22slackUserId");
        String decodedDistrict = URLDecoder.decode(district, "UTF-8");
        String finalDecodedDistrict = Utils.decodeStr(decodedDistrict);

        searchFilter.setDistrict(finalDecodedDistrict);
    }

    private QuestionPromptToSaveSearch getQuestionPrompt(SearchFilter filter) throws JsonProcessingException {
        return QuestionPromptToSaveSearch.builder()
                .fallback("")
                .title("Would you like to save your query?")
                .text("We can send you notifications when there are new apartments :)")
                .callback_id(PROMPT_QUESTION_ID)
                .actions(getActions(filter))
                .build();
    }

    private List<SlackAction> getActions(SearchFilter filter) throws JsonProcessingException {
        List<SlackAction> slackActions = new ArrayList<>();

        SlackAction saveAction = SlackAction.builder()
                .name("Save")
                .text("Yes")
                .type("button")
                .value(objectMapper.writeValueAsString(filter))
                .build();

        SlackAction doNotSaveAction = SlackAction.builder()
                .name("No save")
                .text("No")
                .type("button")
                .value(objectMapper.writeValueAsString(filter))
                .build();

        slackActions.add(saveAction);
        slackActions.add(doNotSaveAction);

        return slackActions;
    }

    private void logSearchFilter(SearchFilter searchFilter, String body) {
        LOGGER.info("Saved query -> district {}, maxPrice {}, rooms {}, minSize {}, slackId {}, rawBody {}",
                searchFilter.getDistrict(),
                searchFilter.getMaxPrice(),
                searchFilter.getMinNumberOfRooms(),
                searchFilter.getMinNumberOfRooms(),
                searchFilter.getSlackUserId(),
                body);
    }
}
