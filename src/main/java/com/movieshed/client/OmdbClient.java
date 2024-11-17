package com.movieshed.client;

import com.movieshed.model.dto.Movie;
import com.movieshed.model.dto.MovieResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutionException;

@Component
@AllArgsConstructor
@Slf4j
public class OmdbClient {
    private WebClient omdbWebClient;

    public MovieResponse searchMovieGetRequest(String movieTitle) {
        try {

            return omdbWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", "fc4b3e6a")
                            .queryParam("s", "\"" + movieTitle + "\"")
                            .build()).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(MovieResponse.class)
                    .subscribeOn(
                            Schedulers.boundedElastic()).toFuture()
                    .get();



        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
