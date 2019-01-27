package com.rooma.scraper.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rooma.scraper.RentalType;
import com.rooma.scraper.listing.Listing;
import com.rooma.scraper.listing.ListingRepository;
import com.sun.tools.javac.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SearchControllerTest {
    private final ListingRepository listingRepository = Mockito.mock(ListingRepository.class);
    private final SearchFilterRepository searchFilterRepository = Mockito.mock(SearchFilterRepository.class);
    private SearchController searchController = new SearchController(listingRepository, searchFilterRepository);

    @Before
    public void setup() {
        mockMvc(MockMvcBuilders.standaloneSetup(searchController).build());
    }

    @Test
    public void respondWithAttachmentWhenListingsAreFound() throws JsonProcessingException {
        when(listingRepository.findBy(anyFloat(), anyString(), anyFloat(), anyFloat())).thenReturn(List.of(Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title("Some title")
                .address("Some address")
                .district("mitte")
                .postcode("10243")
                .numberOfRooms(2f)
                .size(55f)
                .price(700f)
                .source("Craigslist")
                .url("Some url")
                .imageUrl("Some image url")
                .isAvailable(true)
                .build()));

        ResponseEntity<?> responseEntity = searchController.slackSearch("token=YuQzkOjcW0djDVN9ZtAtVas2&team_id=TF2GRLP29&team_domain=myrooma&channel_id=CF25WGVNG&channel_name=test&user_id=UF25WAA8L&user_name=laurie.malau&command=%2Fwohnung&text=charlottenburg+800+1+40&response_url=https%3A%2F%2Fhooks.slack.com%2Fcommands%2FTF2GRLP29%2F534629616229%2FZSNT4TSKdV0ewApbLO6Fgf3m&trigger_id=534426288850.512569703077.244206a5f0b768509cdda2d9613f3a0e");

        assertThat(responseEntity.getStatusCode().toString(), is("200 OK"));
    }

    @Test
    public void respondWithFallbackTextWhenNoListingsAreFound() throws JsonProcessingException {
        ResponseEntity<?> responseEntity = searchController.slackSearch("token=YuQzkOjcW0djDVN9ZtAtVas2&team_id=TF2GRLP29&team_domain=myrooma&channel_id=CF25WGVNG&channel_name=test&user_id=UF25WAA8L&user_name=laurie.malau&command=%2Fwohnung&text=charlottenburg+800+1+40&response_url=https%3A%2F%2Fhooks.slack.com%2Fcommands%2FTF2GRLP29%2F534629616229%2FZSNT4TSKdV0ewApbLO6Fgf3m&trigger_id=534426288850.512569703077.244206a5f0b768509cdda2d9613f3a0e");

        assertThat(responseEntity.getBody().toString(), is("No listings are found for your search criteria :cry:"));
    }

    @Test
    public void respondWithAttachmentWhenSavingSearch() throws IOException {
        when(listingRepository.findBy(anyFloat(), anyString(), anyFloat(), anyFloat())).thenReturn(List.of(Listing.builder()
                .rentalType(RentalType.APARTMENT)
                .title("Some title")
                .address("Some address")
                .district("mitte")
                .postcode("10243")
                .numberOfRooms(2f)
                .size(55f)
                .price(700f)
                .source("Craigslist")
                .url("Some url")
                .imageUrl("Some image url")
                .isAvailable(true)
                .build()));

        ResponseEntity<?> responseEntity = searchController.slackSaveSearchRequest("payload=%7B%22type%22%3A%22interactive_message%22%2C%22actions%22%3A%5B%7B%22name%22%3A%22Save%22%2C%22type%22%3A%22button%22%2C%22value%22%3A%22%7B%5C%22id%5C%22%3Anull%2C%5C%22minNumberOfRooms%5C%22%3A1.0%2C%5C%22maxPrice%5C%22%3A1800.0%2C%5C%22minSize%5C%22%3A50.0%2C%5C%22district%5C%22%3A%5C%22kreuzberg%5C%22%2C%5C%22slackUserId%5C%22%3A%5C%22UF25WAA8L%5C%22%7D%22%7D%5D%2C%22callback_id%22%3A%22prompt_question_id%22%2C%22team%22%3A%7B%22id%22%3A%22TF2GRLP29%22%2C%22domain%22%3A%22myrooma%22%7D%2C%22channel%22%3A%7B%22id%22%3A%22CF25WGVNG%22%2C%22name%22%3A%22test%22%7D%2C%22user%22%3A%7B%22id%22%3A%22UF25WAA8L%22%2C%22name%22%3A%22laurie.malau%22%7D%2C%22action_ts%22%3A%221548630872.548304%22%2C%22message_ts%22%3A%221548630870.001400%22%2C%22attachment_id%22%3A%2213%22%2C%22token%22%3A%22YuQzkOjcW0djDVN9ZtAtVas2%22%2C%22is_app_unfurl%22%3Afalse%2C%22response_url%22%3A%22https%3A%5C%2F%5C%2Fhooks.slack.com%5C%2Factions%5C%2FTF2GRLP29%5C%2F534325630243%5C%2F0OB9owPHvhiLaO1xH1aebWUv%22%2C%22trigger_id%22%3A%22534432068930.512569703077.d933faf59329321617330111fc461d4c%22%7D");

        assertThat(responseEntity.getStatusCode().toString(), is("200 OK"));
    }
}