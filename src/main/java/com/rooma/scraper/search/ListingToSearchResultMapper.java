package com.rooma.scraper.search;

import com.rooma.scraper.listing.Listing;

import java.util.ArrayList;
import java.util.List;

class ListingToSearchResultMapper {
    SearchResult map(Listing listing) {
        return SearchResult.builder()
                .fallback("")
                .title(listing.getTitle())
                .title_link(listing.getUrl())
                .fields(createFields(listing))
                .build();
    }

    private List<AttachmentField> createFields(Listing listing) {
        List<AttachmentField> fields = new ArrayList<>();
        AttachmentField address = AttachmentField.builder()
                .title("Address")
                .value(listing.getAddress())
                .isShort(false)
                .build();
        AttachmentField size = AttachmentField.builder()
                .title("Size")
                .value(listing.getSize().toString())
                .isShort(true)
                .build();
        AttachmentField price = AttachmentField.builder()
                .title("Price")
                .value(listing.getPrice().toString())
                .isShort(true)
                .build();
        AttachmentField numberOfRooms = AttachmentField.builder()
                .title("Number of Rooms")
                .value(listing.getNumberOfRooms().toString())
                .isShort(true)
                .build();
        AttachmentField source = AttachmentField.builder()
                .title("Source")
                .value(listing.getSource())
                .isShort(true)
                .build();

        fields.add(address);
        fields.add(size);
        fields.add(price);
        fields.add(numberOfRooms);
        fields.add(source);

        return fields;
    }
}
