package com.movieshed.impl;

import com.movieshed.model.Movie;
import com.movieshed.model.MovieDiary;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieDiaryRepository;
import com.movieshed.repository.MovieRepository;
import com.movieshed.service.MovieDiaryService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import  java.util.List;
import java.util.UUID;

@Service
public class MovieDiaryServiceImpl implements MovieDiaryService {
    @Autowired
    private MovieDiaryRepository movieDiaryRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieShedUserService movieShedUserService;

    @Override
    public MovieDiary addMovieDiary(UUID userId, UUID movieId, String notes) {
        MovieShedUser user = movieShedUserService.findMovieShedUserById(userId);
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if (user == null || movie == null) {
            throw new IllegalArgumentException("Invalid user or movie ID.");
        }

        MovieDiary movieDiary = new MovieDiary();
        movieDiary.setMovieShedUser(user);
        movieDiary.setMovie(movie);
        movieDiary.setWatchedDate(LocalDateTime.now());
        movieDiary.setNotes(notes);

        return movieDiaryRepository.save(movieDiary);
    }

    @Override
    public List<MovieDiary> getMovieDiariesByUserId(UUID userId) {
        return movieDiaryRepository.findMovieDiariesByMovieShedUserId(userId);
    }

    @Override
    public List<MovieDiary> getMovieDiariesByUserName(String userName) {
        return movieDiaryRepository.findMovieDiariesByMovieShedUserUserName(userName);
    }

    @Override
    public MovieDiary getMovieDiaryByUserIdAndMovieId(UUID userId, UUID movieId) {
        return movieDiaryRepository.findByMovieShedUserIdAndMovieId(userId, movieId);
    }
}
