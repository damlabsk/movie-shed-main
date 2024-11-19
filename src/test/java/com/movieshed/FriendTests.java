package com.movieshed;

import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.FriendService;
import com.movieshed.service.MovieShedUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
class FriendTests {

    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @AfterEach
    public void cleanUp() {
        movieRepository.deleteAll();
        movieShedUserRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        movieRepository.deleteAll();
        movieShedUserRepository.deleteAll();
    }

    @Test
    void checkFriends() {

        // Step 1: Create the first user
        MovieShedUser user = movieShedUserService.createMovieShedUser("damla", "12345", "dajdad");

        // Step 2: Create the second user (friend)
        MovieShedUser friend = movieShedUserService.createMovieShedUser("huma", "142345", "dajd32ad");

        MovieShedUser friend2 = movieShedUserService.createMovieShedUser("ahmet", "1423345", "dajd362ad");

        MovieShedUser userWithFriend = friendService.addFriend(user, friend);
        MovieShedUser userWithFriend2 = friendService.addFriend(friend2, user);

        MovieShedUser friendInDb = movieShedUserService.findMovieShedUserByUserName(friend.getUserName());
        MovieShedUser friendInDb2 = movieShedUserService.findMovieShedUserByUserName(friend2.getUserName());
        MovieShedUser userInDb = movieShedUserService.findMovieShedUserByUserName(user.getUserName());

        List<MovieShedUser> friends = friendService.getFriendsByUserName(user.getUserName());

        assertThat(friends.size(), is(2));
        assertThat(userInDb.getFriends().size(), is(1));
        assertThat(friendInDb.getFriendOfs().size(), is(1));
        assertThat(friendInDb2.getFriends().size(), is(1));
    }
}
