package com.rooma.scraper.listing;

import com.rooma.scraper.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class Listing {
    @Id
    @GeneratedValue(generator="sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "listing_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private RentalType rentalType;
    private String title;
    private String district;
    private String address;
    private String postcode;
    private Float size;
    private Float price;
    private Float numberOfRooms;
    private String url;
    private String imageUrl;
    private String source;
    private boolean isAvailable;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    @PrePersist
    @PreUpdate
    public void setCreationDateAndModificationDateToNow() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        modificationDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", numberOfRooms=" + numberOfRooms +
                ", url='" + url + '\'' +
                '}';
    }

    public String toJson() {
        return String.format(
               "{\n" +
                       "  \"fallback\": \"\",\n" +
                       "  \"title\": \"%s\",\n" +
                       "  \"title_link\": \"%s\",\n" +
                       "  \"fields\": [\n" +
                       "    {\n" +
                       "      \"title\": \"Address\",\n" +
                       "      \"value\": \"%s\",\n" +
                       "      \"short\": false\n" +
                       "    },\n" +
                       "    {\n" +
                       "      \"title\": \"Size\",\n" +
                       "      \"value\": \"%.2f\",\n" +
                       "      \"short\": true\n" +
                       "    },\n" +
                       "    {\n" +
                       "      \"title\": \"Price\",\n" +
                       "      \"value\": \"%.2f\",\n" +
                       "      \"short\": true\n" +
                       "    },\n" +
                       "    {\n" +
                       "      \"title\": \"Number of rooms\",\n" +
                       "      \"value\": \"%.2f\",\n" +
                       "      \"short\": true\n" +
                       "    }\n" +
                       "  ]\n" +
                       "}",
                this.title,
                this.url,
                this.address,
                this.size,
                this.price,
                this.numberOfRooms
        );
    }
}
