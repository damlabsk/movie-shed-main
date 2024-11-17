package com.movieshed.service;

import com.movieshed.model.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    Movie addMovieToUser(UUID userId, String title, String description, String releaseYear);
    List<Movie> findMoviesByUserId(UUID id);
    List<Movie> findMoviesByUserName(String userName);
    List<Movie> findMoviesByUserEmail(String email);
    Movie  findMovieByUserIdAndTitle(UUID id,String title);
    Movie findMovieByUserNameAndTitle(String userName, String title);
    Movie findMovieByEmailAndTitle(String email, String title);

}