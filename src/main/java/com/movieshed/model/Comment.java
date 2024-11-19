package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "comment")
public class Comment extends EntityBase {

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_shed_user_id")
    private MovieShedUser movieShedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
