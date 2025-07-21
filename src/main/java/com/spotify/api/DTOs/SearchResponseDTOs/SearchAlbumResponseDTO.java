package com.spotify.api.DTOs.SearchResponseDTOs;

import com.spotify.api.DTOs.AlbumDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class SearchAlbumResponseDTO {
    List<SearchAlbumDTO> items;
}
