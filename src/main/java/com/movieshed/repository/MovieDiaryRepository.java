package com.movieshed.repository;

import com.movieshed.model.MovieDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieDiaryRepository extends JpaRepository<MovieDiary, UUID> {
    List<MovieDiary> findMovieDiariesByMovieShedUserId(UUID movieShedUserId);

    List<MovieDiary> findMovieDiariesByMovieShedUserUserName(String userName);

    MovieDiary findByMovieShedUserIdAndMovieId(UUID movieShedUserId, UUID movieId);
}
