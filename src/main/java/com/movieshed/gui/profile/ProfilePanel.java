package com.movieshed.gui.profile;

import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieService;
import com.movieshed.gui.register.RegisterPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class ProfilePanel extends JPanel {

    private JLabel userNameLabel;
    private JList<Movie> watchlist;
    private DefaultListModel<Movie> watchlistModel;
    private RegisterPanel registerPanel;

    @Autowired
    private MovieService movieService;


    public ProfilePanel(RegisterPanel registerPanel) {
        this.registerPanel = registerPanel;
        setLayout(new BorderLayout());

        userNameLabel = new JLabel("User: " + getCurrentUserName(), JLabel.LEFT);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        watchlistModel = new DefaultListModel<>();
        watchlist = new JList<>(watchlistModel);
        watchlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane watchlistScroll = new JScrollPane(watchlist);


        add(userNameLabel, BorderLayout.NORTH);
        add(watchlistScroll, BorderLayout.CENTER);

        loadWatchlist();
    }

    private String getCurrentUserName() {
        return registerPanel.getUserName();
    }

    public void loadWatchlist() {
        MovieShedUser currentUser = registerPanel.getCurrentUser();

        if (currentUser != null) {
            List<Movie> userMovies = movieService.findMoviesByUserEmail(currentUser.getEmail());
            watchlistModel.clear();

            for (Movie movie : userMovies) {
                watchlistModel.addElement(movie);
            }
        }
    }
}

