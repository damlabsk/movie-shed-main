package com.movieshed.gui.search;

import com.movieshed.client.OmdbClient;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.model.dto.MovieResponse;
import com.movieshed.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class SearchPanel extends JPanel {
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea searchArea;

    @Autowired
    private MovieService movieService;
    @Autowired
    private OmdbClient omdbClient;

    public SearchPanel(){
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.NORTH);

        searchArea = new JTextArea();
        searchArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(searchArea);
        this.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String movieTitle = searchField.getText();
            MovieResponse movieResponse = omdbClient.searchMovieGetRequest(movieTitle);
            List<MovieDto> movieDtos = movieResponse.getSearch();
            movieDtos.forEach(movieDto -> {
                searchArea.append(movieDto.getTitle() + "\n");
            });
        });
    }
}
