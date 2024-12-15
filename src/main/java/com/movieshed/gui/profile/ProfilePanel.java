package com.movieshed.gui.profile;

import com.movieshed.UserContext;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.FriendService;
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

    @Autowired
    private MovieService movieService;

    @Autowired
    private FriendService friendService;

    private JPanel watchlistEntriesPanel;
    private JPanel friendsEntriesPanel;

    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setBackground(Color.BLACK);

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userNameLabel.setForeground(Color.WHITE);

        userPanel.add(userNameLabel);
        topPanel.add(userPanel, BorderLayout.CENTER);

        JLabel watchlistLabel = new JLabel("Watchlist");
        watchlistLabel.setFont(new Font("Arial", Font.BOLD, 16));
        watchlistLabel.setForeground(Color.WHITE);
        watchlistLabel.setHorizontalAlignment(SwingConstants.CENTER);

        watchlistEntriesPanel = new JPanel(new GridBagLayout());
        watchlistEntriesPanel.setBackground(Color.BLACK);

        JScrollPane watchlistScroll = new JScrollPane(watchlistEntriesPanel);
        watchlistScroll.setBackground(Color.BLACK);

        JPanel watchlistPanel = new JPanel(new BorderLayout());
        watchlistPanel.setBackground(Color.BLACK);
        watchlistPanel.add(watchlistLabel, BorderLayout.NORTH);
        watchlistPanel.add(watchlistScroll, BorderLayout.CENTER);

        JLabel friendsLabel = new JLabel("Friends");
        friendsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        friendsLabel.setForeground(Color.WHITE);
        friendsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        friendsEntriesPanel = new JPanel();
        friendsEntriesPanel.setLayout(new BoxLayout(friendsEntriesPanel, BoxLayout.Y_AXIS));
        friendsEntriesPanel.setBackground(Color.BLACK);

        JScrollPane friendsScroll = new JScrollPane(friendsEntriesPanel);
        friendsScroll.setBackground(Color.BLACK);

        JPanel friendsPanel = new JPanel(new BorderLayout());
        friendsPanel.setBackground(Color.BLACK);
        friendsPanel.add(friendsLabel, BorderLayout.NORTH);
        friendsPanel.add(friendsScroll, BorderLayout.CENTER);

        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BorderLayout());
        mainCenterPanel.setBackground(Color.BLACK);
        mainCenterPanel.add(watchlistPanel, BorderLayout.CENTER);
        mainCenterPanel.add(friendsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(mainCenterPanel, BorderLayout.CENTER);
    }

    public void loadWatchlist() {
        MovieShedUser currentUser = UserContext.getUser();
        userNameLabel.setText("<html><span style='color:red;'>Welcome! </span>" + currentUser.getUserName() + "</html>");
        List<Movie> userMovies = movieService.findMoviesByUserId(currentUser.getId());
        displayWatchlistMovies(userMovies);
    }

    public void loadFriends() {
        MovieShedUser currentUser = UserContext.getUser();
        List<MovieShedUser> friends = currentUser.getFriends();
        displayFriends(friends);
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

    private void displayFriends(List<MovieShedUser> friends) {
        friendsEntriesPanel.removeAll();

        for (MovieShedUser friend : friends) {
            JPanel friendComponent = createFriendComponent(friend);
            friendsEntriesPanel.add(friendComponent);
            friendsEntriesPanel.add(Box.createRigidArea(new Dimension(0,10)));
        }

        friendsEntriesPanel.revalidate();
        friendsEntriesPanel.repaint();
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

        JButton watchedButton = new JButton("Add to Diary");
        watchedButton.setBackground(Color.WHITE);
        watchedButton.setFocusable(false);
        watchedButton.setForeground(Color.BLACK);

        boolean isWatched = movieService.isMovieWatched(movie.getId());

        if (isWatched) {
            watchedButton.setText("Watched");
            watchedButton.setEnabled(false);
        } else {
            watchedButton.setText("Add to Diary");
        }
        watchedButton.addActionListener(e -> {
            if (!watchedButton.getText().equals("Watched")) {
                movieService.setMovieStatus(movie.getId(), true);
                watchedButton.setText("Watched");
                watchedButton.setEnabled(false);
            }
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

    private JPanel createFriendComponent(MovieShedUser friend) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setPreferredSize(new Dimension(200, 50));

        JLabel friendNameLabel = new JLabel(friend.getUserName());
        friendNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        friendNameLabel.setForeground(Color.WHITE);
        friendNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(friendNameLabel, BorderLayout.WEST);

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