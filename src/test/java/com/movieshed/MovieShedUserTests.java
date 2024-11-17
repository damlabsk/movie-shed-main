package com.movieshed;

import com.movieshed.client.OmdbClient;
import com.movieshed.model.MovieShedUser;
import com.movieshed.model.dto.MovieResponse;
import com.movieshed.service.MovieShedUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class MovieShedUserTests {

    @Autowired
    private MovieShedUserService movieShedUserService;

    @Test
    void createUser() {
       MovieShedUser user=movieShedUserService.createMovieShedUser("damla","12345", "dajdad");
       MovieShedUser userInDb=movieShedUserService.findMovieShedUserById(user.getId());

       assertThat(user.getUserName(),is(userInDb.getUserName()));
       assertThat(user.getPassword(),is(userInDb.getPassword()));
       assertThat(user.getEmail(),is(userInDb.getEmail()));
    }
}
