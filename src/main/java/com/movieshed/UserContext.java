package com.movieshed;

import com.movieshed.model.MovieShedUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContext {
    private UserContext() {}

    private static final InheritableThreadLocal<MovieShedUser> currentUser = new InheritableThreadLocal<>();

    public static void setUser(MovieShedUser user) {
        log.info("Setting user to " + user.getUserName());
        currentUser.set(user);
    }

    public static MovieShedUser getUser() {
        return currentUser.get();
    }

    public static void clear(){
        currentUser.remove();
    }
}
