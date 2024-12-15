package com.movieshed.gui.activity;

import com.movieshed.UserContext;
import com.movieshed.model.Activity;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityPanel extends JPanel {

    @Autowired
    private ActivityService activityService;

    private JPanel activityEntriesPanel;

    public ActivityPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel activityLabel = new JLabel("<html><span style='color:white;'>Activity </span><span style='color:red;'>Feed</span></html>");
        activityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        activityLabel.setForeground(Color.WHITE);
        activityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        activityEntriesPanel = new JPanel();
        activityEntriesPanel.setLayout(new BoxLayout(activityEntriesPanel, BoxLayout.Y_AXIS));
        activityEntriesPanel.setBackground(Color.BLACK);

        JScrollPane activityScrollPane = new JScrollPane(activityEntriesPanel);
        activityScrollPane.setBackground(Color.BLACK);

        add(activityLabel, BorderLayout.NORTH);
        add(activityScrollPane, BorderLayout.CENTER);
    }

    public void loadActivities() {
        List<MovieShedUser> allUsers = new ArrayList<>();
        MovieShedUser currentUser = UserContext.getUser();
        List<MovieShedUser> friends = currentUser.getFriends();
        //allUsers.add(currentUser);
        allUsers.addAll(friends);

        activityEntriesPanel.removeAll();

        allUsers.forEach(movieShedUser -> {
            List<Activity> activities = activityService.getActivitiesByUserId(movieShedUser.getId());
            displayActivities(activities);
        });

        activityEntriesPanel.revalidate();
        activityEntriesPanel.repaint();
    }

    private void displayActivities(List<Activity> activities) {
        for (Activity activity : activities) {
            JPanel activityComponent = createActivityComponent(activity);
            activityEntriesPanel.add(activityComponent);
            activityEntriesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private JPanel createActivityComponent(Activity activity) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(400, 150));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(100, 150));

        if(activity.getMovie()!=null){
            String posterUrl = activity.getMovie().getPosterUrl();
            loadPosterImage(posterUrl, posterLabel);
        } else if (activity.getComment().getMovie()!=null) {
            String posterUrl = activity.getComment().getMovie().getPosterUrl();
            loadPosterImage(posterUrl, posterLabel);
        }

        JLabel usernameLabel = new JLabel(activity.getMovieShedUser().getUserName());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);

        JLabel descriptionLabel = new JLabel("<html>" + activity.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.WHITE);

        JLabel createdAtLabel = new JLabel(activity.getCreatedAt().toString());
        createdAtLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        createdAtLabel.setForeground(Color.LIGHT_GRAY);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.BLACK);
        detailsPanel.add(usernameLabel);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(createdAtLabel);

        panel.add(posterLabel, BorderLayout.WEST);
        panel.add(detailsPanel, BorderLayout.CENTER);

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
                Image scaledImage = image.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
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