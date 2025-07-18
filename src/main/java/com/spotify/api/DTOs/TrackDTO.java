package com.spotify.api.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDTO {
    private String id;
    private String name;
    private int duration_ms;
    private String preview_url;
    private boolean is_playable;
    private AlbumDTO album;
}
