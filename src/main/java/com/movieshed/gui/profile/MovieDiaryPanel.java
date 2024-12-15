package com.movieshed.gui.profile;

import com.movieshed.UserContext;
import com.movieshed.gui.register.RegisterPanel;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieDiary;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieDiaryService;
import com.movieshed.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class MovieDiaryPanel extends JPanel {
    private JLabel userNameLabel;
    private JPanel entriesPanel;
    private JScrollPane entriesScroll;

    @Autowired
    private MovieDiaryService movieDiaryService;

    @Autowired
    private MovieService movieService;

    public MovieDiaryPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setForeground(Color.WHITE);

        add(userNameLabel, BorderLayout.NORTH);

        entriesPanel = new JPanel(new GridBagLayout());
        entriesPanel.setBackground(Color.BLACK);

        entriesScroll = new JScrollPane(entriesPanel);
        entriesScroll.setBackground(Color.BLACK);
        entriesScroll.getVerticalScrollBar().setBackground(Color.BLACK);
        add(entriesScroll, BorderLayout.CENTER);
    }

    public void loadWatchedMovies() {
        MovieShedUser user = UserContext.getUser();
        userNameLabel.setText("<html><span style='color:white;'>" + user.getUserName() + "'s </span><span style='color:red;'>Movie Diary</span></html>");
        List<Movie> watchedMovies = movieService.findWatchedMoviesByUserId(user.getId());

        displayWatchedMovies(watchedMovies);
    }

    private void displayWatchedMovies(List<Movie> watchedMovies) {
        entriesPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        int columns = 4;
        int index = 0;

        for (Movie movie : watchedMovies) {
            JPanel movieComponent = createMovieComponent(movie);
            gbc.gridx = index % columns;
            gbc.gridy = index / columns;
            entriesPanel.add(movieComponent, gbc);
            index++;
        }

        entriesPanel.revalidate();
        entriesPanel.repaint();
    }

    private JPanel createMovieComponent(Movie movie) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(180, 320));
        panel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(150, 225));

        String posterUrl = movie.getPosterUrl();
        loadPosterImage(posterUrl, posterLabel);

        JLabel titleLabel = new JLabel("<html><center>" + movie.getTitle() + "</center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(Color.BLACK);

        JButton createDiaryButton = new JButton("Create Diary");
        createDiaryButton.setBackground(Color.WHITE);
        createDiaryButton.setForeground(Color.BLACK);
        createDiaryButton.addActionListener(e -> openCreateDiaryDialog(movie));

        JButton showDiaryButton = new JButton("Show Diary");
        showDiaryButton.setBackground(Color.WHITE);
        showDiaryButton.setForeground(Color.BLACK);
        showDiaryButton.addActionListener(e -> openShowDiaryDialog(movie));

        buttonPanel.add(createDiaryButton);
        buttonPanel.add(showDiaryButton);

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(posterLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

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

    private void openCreateDiaryDialog(Movie movie) {
        JDialog createDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Create Diary for " + movie.getTitle(), true);
        createDialog.setLayout(new BorderLayout());
        createDialog.setSize(400, 300);

        JTextArea notesArea = new JTextArea();
        notesArea.setBackground(Color.DARK_GRAY);
        notesArea.setForeground(Color.WHITE);
        createDialog.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Close");

        saveButton.addActionListener(ev -> {
            String notes = notesArea.getText();
            movieDiaryService.addMovieDiary(UserContext.getUser().getId(), movie.getId(), notes);
            createDialog.dispose();
        });

        cancelButton.addActionListener(ev -> createDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        createDialog.add(buttonPanel, BorderLayout.SOUTH);

        createDialog.setLocationRelativeTo(this);
        createDialog.setVisible(true);
    }

    private void openShowDiaryDialog(Movie movie) {
        JDialog showDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Show Diary for " + movie.getTitle(), true);
        showDialog.setLayout(new BorderLayout());
        showDialog.setSize(400, 300);

        MovieDiary movieDiary = movieDiaryService.getMovieDiaryByUserIdAndMovieId(UserContext.getUser().getId(), movie.getId());

        JTextArea diaryArea = new JTextArea(movieDiary != null ? movieDiary.getNotes() : "No diary found.");
        diaryArea.setEditable(false);
        diaryArea.setBackground(Color.BLACK);
        diaryArea.setForeground(Color.WHITE);
        showDialog.add(new JScrollPane(diaryArea), BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> showDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(okButton);

        showDialog.add(buttonPanel, BorderLayout.SOUTH);

        showDialog.setLocationRelativeTo(this);
        showDialog.setVisible(true);
    }
}