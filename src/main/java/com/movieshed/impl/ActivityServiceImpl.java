package com.movieshed.impl;

import com.movieshed.model.Activity;
import com.movieshed.model.Comment;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.ActivityRepository;
import com.movieshed.repository.CommentRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.ActivityService;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity addMovieActivity(UUID userId, UUID movieId, String description) {
        Activity activity = new Activity();
        activity.setCreatedAt(Instant.now());
        activity.setDescription(description);

        Movie movie = movieService.findMovieByUserIdAndMovieId(userId,movieId);
        activity.setMovie(movie);

        MovieShedUser user = movieShedUserService.findMovieShedUserById(userId);
        activity.setMovieShedUser(user);

        activityRepository.save(activity);

        user.getActivities().add(activity);
        movieShedUserRepository.save(user);

        return activity;
    }

    @Override
    public Activity addMovieActivity(UUID userId, String movieTitle, String description) {
        Activity activity = new Activity();
        activity.setCreatedAt(Instant.now());
        activity.setDescription(description);

        Movie movie = movieService.findMovieByUserIdAndTitle(userId,movieTitle);
        activity.setMovie(movie);

        MovieShedUser user = movieShedUserService.findMovieShedUserById(userId);
        activity.setMovieShedUser(user);

        activityRepository.save(activity);

        user.getActivities().add(activity);
        movieShedUserRepository.save(user);

        return activity;
    }

    @Override
    public Activity addCommentActivity(UUID userId, UUID commentId, String description) {
        Activity activity = new Activity();
        activity.setCreatedAt(Instant.now());
        activity.setDescription(description);

        Comment comment = commentRepository.findById(commentId).orElse(null);
        activity.setComment(comment);

        MovieShedUser user = movieShedUserService.findMovieShedUserById(userId);
        activity.setMovieShedUser(user);

        activityRepository.save(activity);

        user.getActivities().add(activity);
        movieShedUserRepository.save(user);

        return activity;
    }

    @Override
    public List<Activity> getActivitiesByUserId(UUID userId) {
        return activityRepository.findActivitiesByMovieShedUserId(userId);
    }
}
