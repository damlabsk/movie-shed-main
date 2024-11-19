package com.movieshed.repository;

import com.movieshed.model.Comment;
import com.movieshed.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findCommentsByMovieId(UUID movieId);

    List<Comment> findCommentsByMovieTitle(String movieTitle);

}
