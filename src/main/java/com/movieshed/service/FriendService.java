package com.movieshed.service;

import com.movieshed.model.MovieShedUser;

import java.util.List;
import java.util.UUID;

public interface FriendService {
    MovieShedUser addFriend(MovieShedUser user, MovieShedUser friend);
    MovieShedUser addFriend(String userName, String friendUserName);
    List<MovieShedUser> getFriendsByUserId(UUID userId);
    List<MovieShedUser> getFriendsByUserName(String userName);
}