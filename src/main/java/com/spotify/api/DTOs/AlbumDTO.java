package com.spotify.api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private String id;
    private String name;
    private List<ImageDTO> images;
    private String release_date;

    public Integer getReleaseYear() {
        LocalDate date = LocalDate.parse(this.release_date);
        return date.getYear();
    }
}
