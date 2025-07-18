package com.spotify.api.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumTracksResponseDTO {
    private List<TrackDTO> items;
}
