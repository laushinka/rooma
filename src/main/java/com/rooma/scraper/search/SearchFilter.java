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
import org.springframework.web.util.UriUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                ", slackUserId='" + slackUserId + '\'' +
                '}';
    }

    public String sha256() {
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(String.valueOf(this.minNumberOfRooms).getBytes());
            digest.update(String.valueOf(this.maxPrice).getBytes());
            digest.update(String.valueOf(this.minSize).getBytes());
            digest.update(this.district.getBytes());
            digest.update(this.slackUserId.getBytes());

            byte[] encodedhash = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    static SearchFilter buildFilterFromSearchRequestPayload(String body) {
        String payload = StringUtils.substringAfter(body, "text=");
        String district = parseDistrict(payload);
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

    private static String parseDistrict(String payload) {
        String district = payload.split("&")[0].split("\\+")[0];
        String decoded = UriUtils.decode(district, "UTF-8");
        if (decoded.equals("prenzlauerberg")) {
            return "prenzlauer berg";
        }
        return decoded;
    }

    static String buildFilterFromSaveRequestPayload(String decodedResponse) throws UnsupportedEncodingException {
        String searchFiltervalue = StringUtils.substringBetween(decodedResponse, "\"value\":\"", "\"}],\"callback_id\"");
        String responseUrl = StringUtils.substringBetween(decodedResponse, "response_url\":\"", "\"");
        return searchFiltervalue.replaceAll("\\\\", "");
    }

    static boolean doNotSaveSearchFilter(String decodedResponse) throws UnsupportedEncodingException {
        String userChoice = StringUtils.substringBetween(decodedResponse, "\"name\":\"", "\",\"type\"");
        return userChoice.equals("No save");
    }

    static String extractResponseUrl(String decodedResponse) throws UnsupportedEncodingException {
        String responseUrl = StringUtils.substringBetween(decodedResponse, "response_url\":\"", "\"");
        return responseUrl.replaceAll("\\\\", "");
    }
}
