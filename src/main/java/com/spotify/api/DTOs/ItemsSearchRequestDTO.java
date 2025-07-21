package com.spotify.api.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ItemsSearchRequestDTO {
    private String query;
    private String types;

    public ItemsSearchRequestDTO(String query, String types) {
        this.query = query;
        this.types = types;
    }
}
