package com.movieshed.gui.profile;

import com.movieshed.UserContext;
import com.movieshed.gui.register.RegisterPanel;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class ProfilePanel extends JPanel {

    private JLabel userNameLabel;
    private RegisterPanel registerPanel;

    @Autowired
    private MovieService movieService;

    private JPanel watchlistEntriesPanel;

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

        watchlistEntriesPanel = new JPanel(new GridBagLayout());
        watchlistEntriesPanel.setBackground(Color.BLACK);

        JScrollPane watchlistScroll = new JScrollPane(watchlistEntriesPanel);
        watchlistScroll.setBackground(Color.BLACK);

        JPanel watchlistPanel = new JPanel(new BorderLayout());
        watchlistPanel.setBackground(Color.BLACK);
        watchlistPanel.add(watchlistLabel, BorderLayout.NORTH);
        watchlistPanel.add(watchlistScroll, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(watchlistPanel, BorderLayout.CENTER);
    }

    private String getCurrentUserName() {
        return registerPanel.getUserName();
    }

    public void loadWatchlist() {
        MovieShedUser currentUser = UserContext.getUser();
        List<Movie> userMovies = movieService.findMoviesByUserId(currentUser.getId());
        displayWatchlistMovies(userMovies);
    }

    private void displayWatchlistMovies(List<Movie> movies) {
        watchlistEntriesPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        int columns = 4;
        int index = 0;

        for (Movie movie : movies) {
            JPanel movieComponent = createMovieComponent(movie);
            gbc.gridx = index % columns;
            gbc.gridy = index / columns;

            watchlistEntriesPanel.add(movieComponent, gbc);
            index++;
        }

        watchlistEntriesPanel.revalidate();
        watchlistEntriesPanel.repaint();
    }

    private JPanel createMovieComponent(Movie movie) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(180, 300));
        panel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(150, 225));

        String posterUrl = movie.getPosterUrl();
        loadPosterImage(posterUrl, posterLabel);

        JLabel titleLabel = new JLabel("<html><center>" + movie.getTitle() + "</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JButton watchedButton = new JButton("Watched");
        watchedButton.setBackground(Color.WHITE);
        watchedButton.setFocusable(false);
        watchedButton.setForeground(Color.BLACK);
        watchedButton.addActionListener(e -> {
            movieService.setMovieStatus(movie.getId(), true);
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(watchedButton);

        panel.add(posterLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return panel;
    }

    private void loadPosterImage(String imageUrl, JLabel posterLabel) {
        new Thread(() -> {
            try {
                if (imageUrl == null || imageUrl.equals("N/A")) {
                    SwingUtilities.invokeLater(() -> {
                        posterLabel.setText("No Image Available");
                    });
                    return;
                }

                BufferedImage image = ImageIO.read(new URL(imageUrl));
                Image scaledImage = image.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);

                SwingUtilities.invokeLater(() -> {
                    posterLabel.setText("");
                    posterLabel.setIcon(icon);
                });
            } catch (IOException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    posterLabel.setText("Image Load Error");
                });
            }
        }).start();
    }
}