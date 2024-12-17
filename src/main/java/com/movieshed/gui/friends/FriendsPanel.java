package com.movieshed.gui.friends;

import com.movieshed.UserContext;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.FriendService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FriendsPanel extends JPanel {

    @Autowired
    private MovieShedUserService movieShedUserService;

    @Autowired
    private MovieShedUserRepository movieShedUserRepository;

    @Autowired
    private FriendService friendService;

    private JPanel usersListPanel;

    public FriendsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("<html><span style='color:white;'>Add </span><span style='color:red;'>Friends</span></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usersListPanel = new JPanel();
        usersListPanel.setLayout(new BoxLayout(usersListPanel, BoxLayout.Y_AXIS));
        usersListPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(usersListPanel);
        scrollPane.setBackground(Color.BLACK);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadUsers() {
        MovieShedUser currentUser = UserContext.getUser();
        List<MovieShedUser> allUsers = movieShedUserRepository.findAll();

        allUsers.removeIf(user -> user.getId().equals(currentUser.getId()));

        displayUsers(allUsers);
    }

    private void displayUsers(List<MovieShedUser> users) {
        usersListPanel.removeAll();

        for (MovieShedUser user : users) {
            JPanel userComponent = createUserComponent(user);
            usersListPanel.add(userComponent);
            usersListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        usersListPanel.revalidate();
        usersListPanel.repaint();
    }

    private JPanel createUserComponent(MovieShedUser user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setBorder(addPadding(10,10,5,10));
        panel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel(user.getUserName());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0,0,5,10));
        panel.add(usernameLabel);


        JLabel userIdLabel = new JLabel("Mail: " + user.getEmail());
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userIdLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5,0,5,10));
        panel.add(userIdLabel);


        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.setBackground(Color.WHITE);
        addFriendButton.setForeground(Color.BLACK);
        addFriendButton.setFocusable(Boolean.FALSE);
        addFriendButton.addActionListener(e -> {
            if (!addFriendButton.getText().equals("Added")) {
                friendService.addFriend(UserContext.getUser(), user);
                addFriendButton.setText("Added");
                addFriendButton.setEnabled(false);
            }
        });

        panel.add(addFriendButton);

        return panel;
    }

    public static Border addPadding(int top, int left, int bottom, int right) {
        LineBorder lineBorder = new LineBorder(Color.GRAY, 1);
        EmptyBorder emptyBorder = new EmptyBorder(top, left, bottom, right);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        return compoundBorder;
    }
}