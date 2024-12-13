package com.movieshed.impl;

import com.movieshed.model.Comment;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.CommentRepository;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.CommentService;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(UUID userId, UUID movieId, String text) {
        Comment comment = new Comment();
        Movie movie = movieRepository.findById(movieId).orElse(null);
        MovieShedUser user = movieShedUserRepository.findById(userId).orElse(null);
        comment.setMovieShedUser(user);
        comment.setMovie(movie);
        comment.setText(text);
        commentRepository.save(comment);
        movie.getComments().add(comment);
        movieRepository.save(movie);
        user.getComments().add(comment);
        movieShedUserRepository.save(user);
        return comment;
    }

    @Override
    public Comment addComment(UUID userId, String movieTitle, String text) {
        Comment comment = new Comment();
        Movie movie = movieRepository.findMovieByMovieShedUserIdAndTitle(userId, movieTitle);
        MovieShedUser user = movieShedUserRepository.findById(userId).orElse(null);
        comment.setMovieShedUser(user);
        comment.setMovie(movie);
        comment.setText(text);
        commentRepository.save(comment);
        movie.getComments().add(comment);
        movieRepository.save(movie);
        user.getComments().add(comment);
        movieShedUserRepository.save(user);
        return comment;
    }

    @Override
    public List<Comment> getComments(UUID movieId) {
        return commentRepository.findCommentsByMovieId(movieId);
    }

    @Override
    public List<Comment> getCommentsByMovieTitle(String movieTitle) {
        return commentRepository.findCommentsByMovieTitle(movieTitle);
    }
}
