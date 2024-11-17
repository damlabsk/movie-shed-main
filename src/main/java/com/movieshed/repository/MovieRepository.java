package com.movieshed.repository;

import com.movieshed.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Movie findMovieByMovieShedUserIdAndTitle(UUID movieShedUserId,String title);

    Movie findMovieByMovieShedUserEmailAndTitle(String movieShedUserEmail,String title);

    Movie findMovieByMovieShedUserUserNameAndTitle(String movieShedUserUserName,String title);

    List<Movie> findMoviesByMovieShedUserEmail(String userEmail);

    List<Movie> findMoviesByMovieShedUserUserName(String userName);

    List<Movie> findMoviesByMovieShedUserId(UUID movieShedUserId);

}
