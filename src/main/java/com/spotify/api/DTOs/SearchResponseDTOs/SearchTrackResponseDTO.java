package com.spotify.api.DTOs.SearchResponseDTOs;

import com.spotify.api.DTOs.TrackDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchTrackResponseDTO {
    List<TrackDTO> items;
}
