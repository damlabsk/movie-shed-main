package com.movieshed.gui.search;

import com.movieshed.client.OmdbClient;
import com.movieshed.gui.MainFrame;
import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.model.dto.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class SearchPanel extends JPanel {

    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel resultsPanel;

    @Lazy
    @Autowired
    private MainFrame mainFrame;

    @Autowired
    private OmdbClient omdbClient;

    @Autowired
    private MovieInfoPanel movieInfoPanel;

    public SearchPanel() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBackground(Color.BLACK);

        searchLabel = new JLabel("Search:");
        searchField = new JTextField();
        searchButton = new JButton("Search");

        searchField.setPreferredSize(new Dimension(400, 25));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(searchLabel, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);

        searchPanel.add(inputPanel, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        this.add(searchPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String movieTitle = searchField.getText();
            MovieResponse movieResponse = omdbClient.searchMovieGetRequest(movieTitle);

            if (movieResponse != null && movieResponse.getSearch() != null) {
                List<MovieDto> movieDtos = movieResponse.getSearch();
                displaySearchResults(movieDtos);
            } else {
                resultsPanel.removeAll();
                resultsPanel.add(new JLabel("No results found."));
                resultsPanel.revalidate();
                resultsPanel.repaint();
            }
        });
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

        JButton infoButton = new JButton("View Info");
        infoButton.setBackground(Color.DARK_GRAY);
        infoButton.setFocusable(false);
        infoButton.setForeground(Color.WHITE);
        infoButton.addActionListener(e -> {
            showMovieInfoPanel(movieDto);
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(infoButton);

        panel.add(posterLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        //panel.add(watchListButton, BorderLayout.SOUTH);
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
