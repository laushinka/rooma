package com.rooma.scraper.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class SearchFilter {
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

    @Override
    public String toString() {
        return "SearchFilter{" +
                ", minNumberOfRooms=" + minNumberOfRooms +
                ", maxPrice=" + maxPrice +
                ", minSize=" + minSize +
                ", district='" + district + '\'' +
                '}';
    }
}
