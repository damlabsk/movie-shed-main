package com.movieshed.service;

import com.movieshed.model.Movie;
import com.movieshed.model.dto.MovieDto;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    Movie addMovieToUser(UUID userId, String title, String description, String releaseYear, String posterUrl);

    Movie setMovieStatus(UUID movieId, boolean watched);

    List<Movie> findMoviesByUserName(String userName);

    List<Movie> findMoviesByUserId(UUID id);

    List<Movie> findMoviesByUserEmail(String email);
    List<Movie> findWatchedMoviesByUserId(UUID userId);

    Movie findMovieByUserIdAndTitle(UUID id, String title);

    Movie findMovieByUserIdAndMovieId(UUID userId, UUID movieId);

    Movie findMovieByUserNameAndTitle(String userName, String title);

    Movie findMovieByEmailAndTitle(String email, String title);

    List<MovieDto> searchMovieByKey(String key);

    boolean isMovieWatched(UUID movieId);
}