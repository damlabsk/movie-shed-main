package com.movieshed.service;

import com.movieshed.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment addComment(UUID userId, UUID movieId, String text);
    Comment addComment(UUID userId, String movieTitle, String text);
    List<Comment> getComments(UUID movieId);
    List<Comment> getCommentsByMovieTitle(String movieTitle);
}