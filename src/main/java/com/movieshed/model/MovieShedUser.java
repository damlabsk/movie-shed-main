package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "movie_shed_user")
public class MovieShedUser extends EntityBase {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movieShedUser")
    private List<Movie> movies = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id")
    private List<MovieShedUser> friends = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_of_id")
    private List<MovieShedUser> friendOfs = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movieShedUser")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movieShedUser")
    private List<Activity> activities = new ArrayList<>();
}

