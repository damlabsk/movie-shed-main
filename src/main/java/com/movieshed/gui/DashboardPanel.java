package com.movieshed.gui;

import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DashboardPanel extends JPanel {

    private final MovieService movieService;
    private JPanel resultsPanel;
    private JScrollPane scrollPane;

    @Autowired
    private MovieInfoPanel movieInfoPanel;

    @Lazy
    @Autowired
    private MainFrame mainFrame;

    @Autowired
    public DashboardPanel(MovieService movieService) {
        this.movieService = movieService;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel logoLabel = new JLabel("<html><span style='color:white;'>Welcome to movie</span><span style='color:red;'>SHED</span></html>");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.BLACK);
        logoPanel.add(logoLabel);
        add(logoPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setBackground(Color.BLACK);

        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        List<MovieDto> popularMovies = createPopularMovies(movieService);
        displaySearchResults(popularMovies);
    }
    private List<MovieDto> createPopularMovies(MovieService movieService) {
        List<String> popularTitles = List.of("Joker", "Barbie", "Oppenheimer", "The Godfather",
                "The Truman Show", "The Green Mile", "Pulp Fiction", "Forrest Gump","Ice Age","The Cars","Avatar","Titanic","Avengers","The Lion King","Frozen","The Dark Knight","Fight Club","Spirited Away","The Pianist","Parasite","Whiplash","Leon","The Prestige","Casablanca","Memento");

        List<MovieDto> popularMovies = new ArrayList<>();
        List<String> selectedTitles = new ArrayList<>();
        Random rand = new Random();

        while (selectedTitles.size() < 12 && selectedTitles.size() < popularTitles.size()) {
            int randIndex = rand.nextInt(popularTitles.size());
            String randomTitle = popularTitles.get(randIndex);

            if (!selectedTitles.contains(randomTitle)) {
                selectedTitles.add(randomTitle);

                List<MovieDto> searchResults = movieService.searchMovieByKey(randomTitle);
                if (!searchResults.isEmpty()) {
                    popularMovies.add(searchResults.get(0));
                }
            }
        }

        return popularMovies;
    }
    private void displaySearchResults(List<MovieDto> movieDtos) {
        resultsPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        int columns = 4;
        int index = 0;

        for (MovieDto movieDto : movieDtos) {
            JPanel movieComponent = createMovieComponent(movieDto);

            gbc.gridx = index % columns;
            gbc.gridy = index / columns;

            resultsPanel.add(movieComponent, gbc);
            index++;
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    private JPanel createMovieComponent(MovieDto movieDto) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(180, 300));
        panel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(150, 225));

        loadPosterImage(movieDto.getPoster(), posterLabel);

        JLabel titleLabel = new JLabel("<html><center>" + movieDto.getTitle() + "</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JButton infoButton = new JButton("View Info");
        infoButton.setBackground(Color.WHITE);
        infoButton.setFocusable(false);
        infoButton.setForeground(Color.BLACK);
        infoButton.addActionListener(e -> {
            showMovieInfoPanel(movieDto);
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(infoButton);

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
    public void showMovieInfoPanel(MovieDto movieDto) {

        movieInfoPanel.displayMovieInfo(movieDto);

        mainFrame.showMovieInfoPanel(movieDto);

    }
    
}
