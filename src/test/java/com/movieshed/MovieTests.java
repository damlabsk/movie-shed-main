package com.movieshed;

import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class MovieTests {

    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    public void cleanUp() {
        movieRepository.deleteAll();
        movieShedUserRepository.deleteAll();
    }

    @Test
    void addMovieToUser() {
        MovieShedUser user = movieShedUserService.createMovieShedUser("damla", "12345", "dajdad");
        movieService.addMovieToUser(user.getId(), "Joker", "movie", "2024");
        movieService.addMovieToUser(user.getId(), "Joker 2", "movie", "2024");
        MovieShedUser userInDb = movieShedUserService.findMovieShedUserById(user.getId());
        List<Movie> moviesInDb = movieService.findMoviesByUserId(userInDb.getId());
        List<Movie> moviesInDb2 = movieService.findMoviesByUserEmail(userInDb.getEmail());
        List<Movie> moviesInDb3 = movieService.findMoviesByUserName(userInDb.getUserName());


        assertThat(moviesInDb.size(), is(2));
        assertThat(moviesInDb2.size(), is(2));
        assertThat(moviesInDb3.size(), is(2));

    }
}
