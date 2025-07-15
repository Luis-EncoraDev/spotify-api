package com.spotify.api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private String id;
    private String name;
    private List<ImageDTO> images;
    private List<String> genres;
}
