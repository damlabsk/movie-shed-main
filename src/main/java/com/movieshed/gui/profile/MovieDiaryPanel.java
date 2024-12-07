package com.movieshed.gui.profile;

import com.movieshed.gui.register.RegisterPanel;
import com.movieshed.impl.MovieDiaryServiceImpl;
import com.movieshed.model.MovieDiary;
import com.movieshed.model.MovieShedUser;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class MovieDiaryPanel extends JPanel {
    private JLabel userNameLabel;
    private DefaultListModel<String> diaryListModel;
    private JList<String> diaryList;
    private JButton saveButton;
    private JTextArea notesArea;
    private MovieDiaryServiceImpl movieDiaryService;

    private RegisterPanel registerPanel;

    public MovieDiaryPanel(RegisterPanel registerPanel) {
        this.registerPanel = registerPanel;
        setLayout(new BorderLayout());

        setBackground(Color.BLACK);

        MovieShedUser currentUser = registerPanel.getCurrentUser();
        if (currentUser != null) {
            userNameLabel = new JLabel("Welcome, " + currentUser.getUserName());
            userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            userNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            userNameLabel.setForeground(Color.WHITE);
        } else {
            userNameLabel = new JLabel("No user logged in");
            userNameLabel.setForeground(Color.WHITE);
        }

        diaryListModel = new DefaultListModel<>();
        diaryList = new JList<>(diaryListModel);
        diaryList.setBackground(Color.BLACK);
        diaryList.setForeground(Color.WHITE);
        JScrollPane diaryScroll = new JScrollPane(diaryList);

        notesArea = new JTextArea(5, 20);
        notesArea.setBackground(Color.BLACK);
        notesArea.setForeground(Color.WHITE);

        saveButton = new JButton("Save Notes");
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);

        add(userNameLabel, BorderLayout.NORTH);
        add(diaryScroll, BorderLayout.CENTER);
        add(notesArea, BorderLayout.SOUTH);
        add(saveButton, BorderLayout.SOUTH);

        loadDiaryEntries(currentUser);
    }

    private void loadDiaryEntries(MovieShedUser currentUser) {
        if (currentUser != null) {

            List<MovieDiary> movieDiaries = movieDiaryService.getMovieDiariesByUserId(currentUser.getId());
            for (MovieDiary diary : movieDiaries) {
                diaryListModel.addElement(String.valueOf(diary.getMovie()));
            }
        }
    }

}
