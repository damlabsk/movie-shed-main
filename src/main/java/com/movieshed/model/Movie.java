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
@ToString(exclude = "movieShedUser")
@Table(name = "movie")
public class Movie extends EntityBase {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "watched")
    private boolean watched;

    @Column(name = "poster_url")
    private String posterUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_shed_user_id")
    private MovieShedUser movieShedUser;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movie")
    private List<Comment> comments = new ArrayList<>();
}
