package com.spotify.api.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class ItemsSearchRequestDTO {
    private String query;
    private List<String> types;

    public ItemsSearchRequestDTO(String query, List<String> types) {
        this.query = query;
        this.types = types;
    }
}
