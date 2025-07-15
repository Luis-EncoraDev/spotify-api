package com.spotify.api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TopArtistsResponseDTO {
    private List<ArtistDTO> items;
    private int total;
    private int limit;
    private int offset;
    private String href;
    private String next;
    private String previous;
}
