package com.movieshed.impl;

import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieShedUserServiceImpl implements MovieShedUserService {
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;

    @Override
    public MovieShedUser createMovieShedUser(String username, String password, String email) {
        MovieShedUser movieShedUser = new MovieShedUser();
        movieShedUser.setUserName(username);
        movieShedUser.setPassword(password);
        movieShedUser.setEmail(email);

        return movieShedUserRepository.save(movieShedUser);
    }

    @Override
    public MovieShedUser findMovieShedUserById(UUID id) {
        return movieShedUserRepository.findById(id).orElse(null);
    }

    @Override
    public MovieShedUser findMovieShedUserByUserName(String userName) {
        return movieShedUserRepository.findMovieShedUserByUserName(userName);
    }

    @Override
    public MovieShedUser findMovieShedUserByEmail(String email) {
        return movieShedUserRepository.findMovieShedUserByEmail(email);
    }

    @Override
    public MovieShedUser validateUser(String username, String password, String email) {
            MovieShedUser user = movieShedUserRepository.findMovieShedUserByUserName(username);
            if(user != null && user.getPassword().equals(password) && user.getEmail().equals(email)){
                return user;
            }
            return null;
    }
}
