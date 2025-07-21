package com.spotify.api.DTOs.SearchResponseDTOs;

import com.spotify.api.DTOs.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAlbumDTO {
    private String id;
    private String name;
    private int total_tracks;
    private List<ImageDTO> images;
    private String release_date;
    private List<SearchArtistDTO> artists;

    public Integer getReleaseYear() {
        LocalDate date = LocalDate.parse(this.release_date);
        return date.getYear();
    }
}
