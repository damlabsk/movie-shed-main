package com.movieshed.service;

import com.movieshed.model.MovieDiary;

import java.util.List;
import java.util.UUID;

public interface MovieDiaryService {
    MovieDiary addMovieDiary(UUID userId, UUID movieId, String notes);

    List<MovieDiary> getMovieDiariesByUserId(UUID userId);

    List<MovieDiary> getMovieDiariesByUserName(String userName);

    MovieDiary getMovieDiaryByUserIdAndMovieId(UUID userId, UUID movieId);

    void updateMovieDiary(UUID userId, UUID movieId, String updatedNotes);
}
