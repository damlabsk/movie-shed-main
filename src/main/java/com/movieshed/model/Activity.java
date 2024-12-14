package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString(exclude = "movieShedUser")
@Table(name = "activity")
public class Activity extends EntityBase {
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_shed_user_id")
    private MovieShedUser movieShedUser;
}
