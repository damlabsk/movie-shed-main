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
        setBackground(Color.BLACK);


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);


        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setBackground(Color.BLACK);

        userNameLabel = new JLabel(getCurrentUserName());
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userNameLabel.setForeground(Color.WHITE);

        JButton movieDiaryButton = new JButton("Movie Diary");
        movieDiaryButton.setFont(new Font("Arial", Font.BOLD, 14));
        movieDiaryButton.setBackground(Color.WHITE);
        movieDiaryButton.setForeground(Color.BLACK);

        movieDiaryButton.addActionListener(e -> openMovieDiaryPanel());

        userPanel.add(userNameLabel);
        userPanel.add(movieDiaryButton);

        JPanel addFriendsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addFriendsPanel.setBackground(Color.BLACK);

        JTextField addFriendsField = new JTextField(15);
        addFriendsField.setFont(new Font("Arial", Font.PLAIN, 14));
        addFriendsField.setBackground(Color.BLACK);
        addFriendsField.setForeground(Color.WHITE);

        JButton addFriendsButton = new JButton("Add friends");
        addFriendsButton.setFont(new Font("Arial", Font.BOLD, 14));
        addFriendsButton.setBackground(Color.WHITE);
        addFriendsButton.setForeground(Color.BLACK);

        addFriendsPanel.add(addFriendsField);
        addFriendsPanel.add(addFriendsButton);


        JButton showFriendsActivityButton = new JButton("Show friends activity");
        showFriendsActivityButton.setFont(new Font("Arial", Font.BOLD, 14));
        showFriendsActivityButton.setBackground(Color.WHITE);
        showFriendsActivityButton.setForeground(Color.BLACK);


        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(Color.BLACK);

        controlsPanel.add(addFriendsPanel, BorderLayout.NORTH);
        controlsPanel.add(showFriendsActivityButton, BorderLayout.SOUTH);

        topPanel.add(userPanel, BorderLayout.WEST);
        topPanel.add(controlsPanel, BorderLayout.EAST);

        JLabel watchlistLabel = new JLabel("Watchlist");
        watchlistLabel.setFont(new Font("Arial", Font.BOLD, 16));
        watchlistLabel.setForeground(Color.WHITE);
        watchlistLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel watchlistPanel = new JPanel(new BorderLayout());
        watchlistPanel.setBackground(Color.BLACK);

        watchlistModel = new DefaultListModel<>();
        watchlist = new JList<>(watchlistModel);
        watchlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        watchlist.setBackground(Color.BLACK);
        watchlist.setForeground(Color.WHITE);
        watchlist.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane watchlistScroll = new JScrollPane(watchlist);
        watchlistPanel.add(watchlistLabel, BorderLayout.NORTH);
        watchlistPanel.add(watchlistScroll, BorderLayout.CENTER);


        add(topPanel, BorderLayout.NORTH);
        add(watchlistPanel, BorderLayout.CENTER);

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

    public void openMovieDiaryPanel() {
        RegisterPanel registerPanel = new RegisterPanel();
        MovieDiaryPanel movieDiaryPanel = new MovieDiaryPanel(registerPanel);

        JFrame movieDiaryFrame = new JFrame("Movie Diary");
        movieDiaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieDiaryFrame.setSize(600, 400);
        movieDiaryFrame.add(movieDiaryPanel);
        movieDiaryFrame.setVisible(true);
    }
}