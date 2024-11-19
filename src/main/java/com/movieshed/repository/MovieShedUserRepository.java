package com.movieshed.repository;

import com.movieshed.model.MovieShedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieShedUserRepository extends JpaRepository<MovieShedUser, UUID> {
    MovieShedUser findMovieShedUserByUserName(String username);

    MovieShedUser findMovieShedUserByEmail(String email);

    MovieShedUser findMovieShedUserById(UUID id);

    @Query("SELECT u.friends FROM MovieShedUser u WHERE u.userName = :movieShedUserUserName")
    List<MovieShedUser> findFriendsByMovieShedUserUserName(@Param("movieShedUserUserName") String movieShedUserUserName);

    @Query("SELECT u.friends FROM MovieShedUser u WHERE u.id = :movieShedUserId")
    List<MovieShedUser> findFriendsByMovieShedUserId(@Param("movieShedUserId") UUID movieShedUserId);
}
