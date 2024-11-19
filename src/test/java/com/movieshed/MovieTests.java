package com.movieshed;

import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
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
        List<MovieDto> movieDtos = movieService.searchMovieByKey("Joker");

        log.info("Found {} movies", movieDtos);
        MovieShedUser user = movieShedUserService.createMovieShedUser("damla", "12345", "dajdad");
        MovieShedUser user2 = movieShedUserService.createMovieShedUser("ahmet", "12345", "dajdad");
        MovieDto movieDto = movieDtos.get(0);
        MovieDto movieDto2 = movieDtos.get(1);
        movieService.addMovieToUser(user.getId(), movieDto.getTitle(), "Movie of " + movieDto.getTitle(), movieDto.getYear(), movieDto.getPoster());
        movieService.addMovieToUser(user.getId(), movieDto2.getTitle(), "Movie of " + movieDto2.getTitle(), movieDto2.getYear(), movieDto2.getPoster());
        movieService.addMovieToUser(user2.getId(), movieDto.getTitle(), "Movie of " + movieDto.getTitle(), movieDto.getYear(), movieDto.getPoster());
        MovieShedUser userInDb = movieShedUserService.findMovieShedUserById(user.getId());
        MovieShedUser userInDb2 = movieShedUserService.findMovieShedUserById(user2.getId());
        List<Movie> moviesOfUser = movieService.findMoviesByUserId(userInDb.getId());
        List<Movie> moviesOfUser2 = movieService.findMoviesByUserId(userInDb2.getId());
        assertThat(moviesOfUser.size(), is(2));
        assertThat(moviesOfUser2.size(), is(1));
        assertThat(userInDb.getMovies().size(), is(2));
        assertThat(userInDb2.getMovies().size(), is(1));

        movieService.setMovieStatus(moviesOfUser.get(0).getId(), true);
        List<Movie> watchedMovies = movieService.findWatchedMoviesByUserId(user.getId());
        List<Movie> watchedMovies2 = movieService.findWatchedMoviesByUserId(user2.getId());
        assertThat(watchedMovies.size(), is(1));
        assertThat(watchedMovies2.size(), is(0));



    }
}
