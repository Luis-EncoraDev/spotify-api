package com.spotify.api.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaylistDTO {
    String id;
    String description;
    List<ImageDTO> images;
    String name;
    PlaylistOwnerDTO owner;
    PlaylistTracksDTO tracks;
}
