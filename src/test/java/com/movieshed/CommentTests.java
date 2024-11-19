package com.movieshed;

import com.movieshed.model.Comment;
import com.movieshed.model.EntityBase;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.CommentRepository;
import com.movieshed.repository.MovieRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.CommentService;
import com.movieshed.service.FriendService;
import com.movieshed.service.MovieService;
import com.movieshed.service.MovieShedUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class CommentTests {

    @Autowired
    private MovieShedUserService movieShedUserService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MovieShedUserRepository movieShedUserRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    public void cleanUp() {
        commentRepository.deleteAll();
        movieRepository.deleteAll();
        movieShedUserRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        movieRepository.deleteAll();
        movieShedUserRepository.deleteAll();
    }

    @Test
    void writeComment() {
        MovieShedUser user = movieShedUserService.createMovieShedUser("damla", "12345", "dajdad");
        MovieShedUser user2 = movieShedUserService.createMovieShedUser("ad", "12345", "dajdad");

        Movie joker = movieService.addMovieToUser(user.getId(), "Joker", "movie", "2024","");
        Comment comment = commentService.addComment(user.getId(), joker.getId(), "comment");
        Comment comment2 = commentService.addComment(user.getId(), joker.getId(), "yorum 2");
        Comment comment3 = commentService.addComment(user2.getId(), joker.getId(), "yorum 3");
        Movie jokerInDb = movieService.findMovieByUserIdAndTitle(user.getId(), "Joker");
        MovieShedUser userInDb = movieShedUserRepository.findMovieShedUserById(user.getId());
        MovieShedUser userInDb2 = movieShedUserRepository.findMovieShedUserById(user2.getId());
        List<UUID> movieCommentIds = jokerInDb.getComments().stream().map(EntityBase::getId).toList();
        List<UUID> user1CommentIds = userInDb.getComments().stream().map(EntityBase::getId).toList();
        List<UUID> user2CommentIds = userInDb2.getComments().stream().map(EntityBase::getId).toList();
        List<UUID> userCommentsId = new ArrayList<>();
        userCommentsId.addAll(user2CommentIds);
        userCommentsId.addAll(user1CommentIds);
        List<Comment> commentsInDb = commentService.getComments(jokerInDb.getId());
        List<UUID> commentIds= commentsInDb.stream().map(EntityBase::getId).toList();
        assertThat(List.of(comment.getId(),comment2.getId(),comment3.getId()), containsInAnyOrder(movieCommentIds.toArray()));
        assertThat(List.of(comment.getId(),comment2.getId(),comment3.getId()), containsInAnyOrder(userCommentsId.toArray()));
        assertThat(List.of(comment.getId(),comment2.getId(),comment3.getId()), containsInAnyOrder(commentIds.toArray()));


    }
}
