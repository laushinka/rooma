package com.rooma.scraper.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class SearchFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilter.class);

    @Id
    @GeneratedValue(generator="sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "search_filter_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private Float minNumberOfRooms;
    private Float maxPrice;
    private Float minSize;
    private String district;
    private String slackUserId;

    @Override
    public String toString() {
        return "SearchFilter{" +
                "id=" + id +
                ", minNumberOfRooms=" + minNumberOfRooms +
                ", maxPrice=" + maxPrice +
                ", minSize=" + minSize +
                ", district='" + district + '\'' +
                '}';
    }

    static SearchFilter buildFilterFromSearchRequestPayload(String body) {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = payload.split("&")[0].split("\\+")[0];
        Float price = Float.valueOf(payload.split("&")[0].split("\\+")[1]);
        Float numberOfRooms = Float.valueOf(payload.split("&")[0].split("\\+")[2]);
        Float minSize = Float.valueOf(payload.split("&")[0].split("\\+")[3]);
        String slackUserId = StringUtils.substringBetween(body, "user_id=", "&user_name");

        LOGGER.info("Searched district = {}, maxPrice = {}, numberOfRooms = {}, minSize = {}, slackId = {}", district, price, numberOfRooms, minSize, slackUserId);

        return SearchFilter.builder()
                .district(district)
                .maxPrice(price)
                .minNumberOfRooms(numberOfRooms)
                .minSize(minSize)
                .slackUserId(slackUserId)
                .build();
    }

    static String buildFilterFromSaveRequestPayload(String body) throws UnsupportedEncodingException {
        String decodedResponse = URLDecoder.decode(body, "UTF-8");
        String result = StringUtils.substringBetween(decodedResponse, "\"value\":\"", "\"}],\"callback_id\"");
        return result.replaceAll("\\\\", "");
    }
}
