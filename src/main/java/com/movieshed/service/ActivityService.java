package com.movieshed.service;

import com.movieshed.model.Activity;

import java.util.List;
import java.util.UUID;

public interface ActivityService {
    Activity addMovieActivity(UUID userId, UUID movieId, String description);
    Activity addMovieActivity(UUID userId, String movieTitle, String description);
    Activity addCommentActivity(UUID userId, UUID commentId, String description);
    List<Activity> getActivitiesByUserId(UUID userId);
}