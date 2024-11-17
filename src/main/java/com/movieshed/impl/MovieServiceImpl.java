package com.movieshed.impl;

import com.movieshed.client.OmdbClient;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private OmdbClient omdbClient;

    @Override
    public Movie addMovieToUser(UUID userId, String title, String description, String releaseYear) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseYear(releaseYear);
        movieRepository.save(movie);

        MovieShedUser movieShedUser = movieShedUserService.findMovieShedUserById(userId);
        movie.setMovieShedUser(movieShedUser);
        movieRepository.save(movie);

        movieShedUser.getMovies().add(movie);
        movieShedUserRepository.save(movieShedUser);

        return movie;
    }

    @Override
    public List<Movie> findMoviesByUserId(UUID id) {
       return movieRepository.findMoviesByMovieShedUserId(id);
    }

    @Override
    public List<Movie> findMoviesByUserName(String userName) {
        return movieRepository.findMoviesByMovieShedUserUserName(userName);
    }

    @Override
    public List<Movie> findMoviesByUserEmail(String email) {
        return movieRepository.findMoviesByMovieShedUserEmail(email);
    }

    @Override
    public Movie findMovieByUserIdAndTitle(UUID id, String title) {
        return movieRepository.findMovieByMovieShedUserIdAndTitle(id, title);
    }

    @Override
    public Movie findMovieByUserNameAndTitle(String userName, String title) {
        return movieRepository.findMovieByMovieShedUserUserNameAndTitle(userName, title);
    }

    @Override
    public Movie findMovieByEmailAndTitle(String email, String title) {
        return movieRepository.findMovieByMovieShedUserEmailAndTitle(email, title);
    }
}
