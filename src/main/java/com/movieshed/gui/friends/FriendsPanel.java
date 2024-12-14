package com.movieshed.gui.friends;

import com.movieshed.UserContext;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.FriendService;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
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

        JLabel titleLabel = new JLabel("Add Friends");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

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
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setPreferredSize(new Dimension(300, 50));

        JLabel usernameLabel = new JLabel(user.getUserName());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.setBackground(Color.WHITE);
        addFriendButton.setForeground(Color.BLACK);
        addFriendButton.addActionListener(e -> {
            friendService.addFriend(UserContext.getUser(), user);
            addFriendButton.setText("Added");
            addFriendButton.setEnabled(false);
        });

        panel.add(usernameLabel, BorderLayout.WEST);
        panel.add(addFriendButton, BorderLayout.EAST);

        return panel;
    }
}