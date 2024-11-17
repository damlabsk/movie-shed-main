package com.movieshed.repository;

import com.movieshed.model.MovieShedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieShedUserRepository extends JpaRepository<MovieShedUser, UUID> {
    MovieShedUser findMovieShedUserByUserName(String username);

    MovieShedUser findMovieShedUserByEmail(String email);
}
