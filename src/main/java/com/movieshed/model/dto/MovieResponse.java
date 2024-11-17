package com.movieshed.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MovieResponse {
    @JsonProperty("Search")
    private List<Movie> search;
    private String totalResults;
    @JsonProperty("Response")
    private String response;
}

