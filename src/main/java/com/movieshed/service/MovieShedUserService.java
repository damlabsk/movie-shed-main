package com.movieshed.service;

import com.movieshed.model.MovieShedUser;

import java.util.UUID;

public interface MovieShedUserService {
    MovieShedUser createMovieShedUser(String username, String password, String email);

    MovieShedUser findMovieShedUserById(UUID id);

    MovieShedUser findMovieShedUserByUserName(String userName);

    MovieShedUser findMovieShedUserByEmail(String email);

    MovieShedUser validateUser(String username, String password, String email);
}