package com.spotify.api.DTOs.SearchResponseDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDTO {
    SearchArtistResponseDTO artists;
    SearchTrackResponseDTO tracks;
    SearchAlbumResponseDTO albums;
    SearchPlaylistResponseDTO playlists;
}
