package com.spotify.api.DTOs.SearchResponseDTOs;

import com.spotify.api.DTOs.ArtistDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchArtistResponseDTO {
    List<ArtistDTO> items;
}
