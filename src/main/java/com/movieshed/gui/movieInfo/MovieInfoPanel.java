package com.movieshed.gui.movieInfo;

import com.movieshed.UserContext;
import com.movieshed.gui.profile.ProfilePanel;
import com.movieshed.model.Comment;
import com.movieshed.model.Movie;
import com.movieshed.model.MovieShedUser;
import com.movieshed.model.dto.MovieDto;
import com.movieshed.service.ActivityService;
import com.movieshed.service.CommentService;
import com.movieshed.service.MovieDiaryService;
import com.movieshed.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.List;

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
    private ActivityService activityService;

    @Autowired
    private MovieDiaryService movieDiaryService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProfilePanel profilePanel;

    private JTextArea commentTextArea;
    private JButton addCommentButton;

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
        addMovieButton.setFocusable(false);
        addMovieButton.setForeground(Color.WHITE);
        addMovieButton.setBackground(new Color(139, 0, 0));
        addMovieButton.addActionListener(e -> {
            MovieShedUser user = UserContext.getUser();
            Movie movie = movieService.addMovieToUser(user.getId(), currentMovieDto.getTitle(), "asdf", currentMovieDto.getYear(), currentMovieDto.getPoster());
            activityService.addMovieActivity(user.getId(), movie.getId(), user.getUserName() + " added " + movie.getTitle() +" to their watchlist");
            JOptionPane.showMessageDialog(this, titleLabel.getText() + " added to WatchList!");
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

        commentTextArea = new JTextArea();
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane commentInputScrollPane = new JScrollPane(commentTextArea);
        commentInputScrollPane.setBounds(20, 560, 600, 80);
        add(commentInputScrollPane);

        addCommentButton = new JButton("Add Comment");
        addCommentButton.setFont(new Font("Arial", Font.BOLD, 15));
        addCommentButton.setForeground(Color.WHITE);
        addCommentButton.setBackground(new Color(139, 0, 0));
        addCommentButton.setFocusable(false);
        addCommentButton.setBounds(630, 560, 120, 40);
        add(addCommentButton);

        addCommentButton.addActionListener(e -> addComment());
    }

    public void displayMovieInfo(MovieDto movieDto) {
        currentMovieDto = movieDto;
        titleLabel.setText(movieDto.getTitle());
        yearLabel.setText(" ● " + movieDto.getYear());

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

        loadComments(movieDto.getTitle());
    }

    private void loadComments(String movieTitle) {
        commentsPanel.removeAll();

        List<Comment> comments = commentService.getCommentsByMovieTitle(movieTitle);
        if (comments == null || comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No Comments Found");
            noCommentsLabel.setForeground(Color.WHITE);
            noCommentsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            commentsPanel.add(noCommentsLabel);
        } else {
            for (Comment comment : comments) {
                JPanel singleCommentPanel = new JPanel();
                singleCommentPanel.setLayout(new BoxLayout(singleCommentPanel, BoxLayout.Y_AXIS));
                singleCommentPanel.setBackground(Color.BLACK);
                singleCommentPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

                JLabel timestampLabel = new JLabel(Instant.now().toString());
                timestampLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                timestampLabel.setForeground(Color.LIGHT_GRAY);

                JLabel userLabel = new JLabel("User: " + comment.getMovieShedUser().getUserName());
                userLabel.setFont(new Font("Arial", Font.BOLD, 14));
                userLabel.setForeground(Color.WHITE);

                JLabel commentTextLabel = new JLabel("<html><p style='width:700px; color: white;'>" + comment.getText() + "</p></html>");
                commentTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                singleCommentPanel.add(timestampLabel);
                singleCommentPanel.add(userLabel);
                singleCommentPanel.add(Box.createVerticalStrut(5));
                singleCommentPanel.add(commentTextLabel);

                commentsPanel.add(singleCommentPanel);
                commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    private void addComment() {
        String commentText = commentTextArea.getText().trim();
        if (commentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MovieShedUser user = UserContext.getUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to comment!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Comment comment = commentService.addComment(user.getId(), currentMovieDto.getTitle(), commentText);
            activityService.addCommentActivity(user.getId(), comment.getId(), user.getUserName() + " added comment to " + comment.getMovie().getTitle());
            commentTextArea.setText("");
            loadComments(currentMovieDto.getTitle());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add comment!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}