package com.movieshed.impl;

import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.FriendService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private MovieShedUserService movieShedUserService;

    @Override
    public MovieShedUser addFriend(MovieShedUser user, MovieShedUser friend) {
        user.getFriends().add(friend);
        friend.getFriendOfs().add(user);
        movieShedUserRepository.save(user);
        movieShedUserRepository.save(friend);
        return user;
    }

    @Override
    public MovieShedUser addFriend(String userName, String friendUserName) {
        MovieShedUser user = movieShedUserService.findMovieShedUserByUserName(userName);
        MovieShedUser friend = movieShedUserService.findMovieShedUserByUserName(friendUserName);
        user.getFriends().add(friend);
        friend.getFriendOfs().add(user);
        movieShedUserRepository.save(user);
        movieShedUserRepository.save(friend);
        return user;
    }



    @Override
    public List<MovieShedUser> getFriendsByUserId(UUID userId) {
        List<MovieShedUser> friends = new ArrayList<>();
        MovieShedUser user = movieShedUserService.findMovieShedUserById(userId);
        friends.addAll(user.getFriends());
        friends.addAll(user.getFriendOfs());
        return friends;
    }

    @Override
    public List<MovieShedUser> getFriendsByUserName(String userName) {
        List<MovieShedUser> friends = new ArrayList<>();
        MovieShedUser user = movieShedUserService.findMovieShedUserByUserName(userName);
        friends.addAll(user.getFriends());
        friends.addAll(user.getFriendOfs());
        return friends;
    }
}
