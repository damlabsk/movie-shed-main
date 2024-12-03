package com.movieshed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "movie_diary")
public class MovieDiary extends EntityBase{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_shed_user_id", nullable = false)
    private MovieShedUser movieShedUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "watched_date", nullable = false)
    private LocalDateTime watchedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
