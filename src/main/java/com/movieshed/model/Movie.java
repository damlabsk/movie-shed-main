package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "movie")
public class Movie extends EntityBase {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "watched")
    private Boolean watched;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_shed_user_id")
    private MovieShedUser movieShedUser;
}
