package com.spotify.api.DTOs.SearchResponseDTOs;

import com.spotify.api.DTOs.PlaylistDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SearchPlaylistResponseDTO {
    List<PlaylistDTO> items;
}
