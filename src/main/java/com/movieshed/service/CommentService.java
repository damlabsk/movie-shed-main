package com.movieshed.service;

import com.movieshed.model.Comment;
import com.movieshed.model.Movie;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment addComment(UUID userId, UUID movieId, String text);
    List<Comment> getComments(UUID movieId);
}