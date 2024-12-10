package com.movieshed.gui.movieInfo;

import com.movieshed.UserContext;
import com.movieshed.gui.profile.ProfilePanel;
import com.movieshed.model.MovieShedUser;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.service.CommentService;
import com.movieshed.service.MovieDiaryService;
import com.movieshed.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@Component
public class MovieInfoPanel extends JPanel {
    private JPanel infoPanel;
    private JLabel titleLabel;
    private JLabel yearLabel;
    private JLabel posterLabel;
    private JButton addMovieButton;
    private JPanel commentsPanel;
    private JScrollPane commentsScrollPane;
    private MovieDto currentMovieDto;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MovieDiaryService movieDiaryService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProfilePanel profilePanel;

    public MovieInfoPanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        posterLabel = new JLabel("", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(200, 300)); // Set poster size
        add(posterLabel);
        posterLabel.setBounds(20, 20, 200, 300);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        yearLabel = new JLabel();
        yearLabel.setFont(new Font("Arial", Font.BOLD, 24));
        yearLabel.setForeground(Color.WHITE);

        addMovieButton = new JButton("+ Add to WatchList");
        addMovieButton.setFont(new Font("Arial", Font.BOLD, 15));
        addMovieButton.setFocusable(Boolean.FALSE);
        addMovieButton.setForeground(Color.WHITE);
        addMovieButton.setBackground(new Color(139, 0, 0));
        addMovieButton.addActionListener(e -> {
            MovieShedUser user = UserContext.getUser();
            movieService.addMovieToUser(user.getId(), currentMovieDto.getTitle(), "asdf", currentMovieDto.getYear(), currentMovieDto.getPoster());
            JOptionPane.showMessageDialog(this, titleLabel.getText() + " added to WatchList!");
            addMovieButton.setText("+ Add to Movie Dairy");
        });

        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(titleLabel);
        infoPanel.add(yearLabel);
        add(infoPanel);
        infoPanel.setBounds(250, 20, 500, 70);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(addMovieButton);
        add(buttonPanel);
        buttonPanel.setBounds(250, 90, 200, 50);

        commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(Color.BLACK);

        commentsScrollPane = new JScrollPane(commentsPanel);
        commentsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        commentsScrollPane.setBounds(20, 350, 730, 200);
        add(commentsScrollPane);
    }

    public void displayMovieInfo(MovieDto movieDto) {
        currentMovieDto = movieDto;
        titleLabel.setText(movieDto.getTitle());
        yearLabel.setText(" ● " + movieDto.getYear());
        //plotLabel.setText("<html><p style='width: 400px;'>" + movieDto.getType() + "</p></html>");

        if (movieDto.getPoster() != null && !movieDto.getPoster().equals("N/A")) {
            new Thread(() -> {
                try {
                    ImageIcon icon = new ImageIcon(new URL(movieDto.getPoster()));
                    Image scaledImage = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> posterLabel.setIcon(new ImageIcon(scaledImage)));
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> posterLabel.setText("Image Load Error"));
                }
            }).start();
        } else {
            posterLabel.setText("No Image Available");
        }
        //System.out.println(movieDto.getImdbID());
        loadComments(movieDto.getTitle());
    }

    private void loadComments(String movieTitle) {
        commentsPanel.removeAll();
//        List<Comment> comments = commentService.getCommentsByTitle(movieTitle);
//        if (comments == null && comments.isEmpty()) {
//            JLabel noCommentsLabel = new JLabel("No Comments Found");
//            noCommentsLabel.setForeground(Color.WHITE);
//            commentsPanel.add(noCommentsLabel);
//        } else {
//            for (Comment comment : comments) {
//                JLabel commentLabel = new JLabel("\"<html><p style='color: white;'>\" + comment.getText() + \"</p></html>\"");
//                commentLabel.setFont(new Font("Arial", Font.BOLD, 14));
//                commentsPanel.add(commentLabel);
//                commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//            }
//        }
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }
}
