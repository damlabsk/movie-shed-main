package com.movieshed.gui;

import com.movieshed.gui.activity.ActivityPanel;
import com.movieshed.gui.chat.ChatPanel;
import com.movieshed.gui.friends.FriendsPanel;
import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.gui.profile.MovieDiaryPanel;
import com.movieshed.gui.profile.ProfilePanel;
import com.movieshed.gui.login.LoginPanel;
import com.movieshed.gui.register.RegisterPanel;
import com.movieshed.gui.search.SearchPanel;
import com.movieshed.model.dto.MovieDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {

    private JPanel contentPane;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private JPanel toolbar;

    @Autowired
    private RegisterPanel registerPanel;

    @Autowired
    private DashboardPanel dashboardPanel;

    @Autowired
    private SearchPanel searchPanel;

    @Autowired
    private MovieInfoPanel movieInfoPanel;

    @Autowired
    private MovieDiaryPanel movieDiaryPanel;

    @Autowired
    private ProfilePanel profilePanel;

    @Autowired
    private ActivityPanel activityPanel;

    @Autowired
    private FriendsPanel friendsPanel;

    @Autowired
    private LoginPanel loginPanel;

    @Autowired
    private ChatPanel chatPanel;

    public MainFrame() {
        setTitle("Movie Shed");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    @PostConstruct
    public void init() {
        contentPane = new JPanel(new BorderLayout());

        toolbar = createToolbar();

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);

        contentArea.add(loginPanel, "loginPanel");
        contentArea.add(registerPanel, "registerPanel");
        contentArea.add(dashboardPanel, "dashboardPanel");
        contentArea.add(searchPanel, "searchPanel");
        contentArea.add(profilePanel, "profilePanel");
        contentArea.add(movieDiaryPanel, "movieDiaryPanel");
        contentArea.add(movieInfoPanel, "movieInfoPanel");
        contentArea.add(activityPanel, "activityPanel");
        contentArea.add(friendsPanel, "friendsPanel");
        contentArea.add(chatPanel, "chatPanel");

        loginPanel.setOnRegisterSuccess(this::onRegisterSuccess);

        contentPane.add(contentArea, BorderLayout.CENTER);

        setContentPane(contentPane);
    }

    private JPanel createToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.BLACK);

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.setBackground(new Color(128, 128, 128));
        dashboardButton.setForeground(Color.BLACK);
        dashboardButton.setFocusable(Boolean.FALSE);
        dashboardButton.addActionListener(e -> showDashboardPanel());

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(128, 128, 128));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusable(Boolean.FALSE);
        searchButton.addActionListener(e -> showSearchPanel());

        JButton profileButton = new JButton("Profile");
        profileButton.setBackground(new Color(128, 128, 128));
        profileButton.setForeground(Color.BLACK);
        profileButton.setFocusable(Boolean.FALSE);
        profileButton.addActionListener(e -> showProfilePanel());

        JButton diaryButton = new JButton("Diary");
        diaryButton.setBackground(new Color(128, 128, 128));
        diaryButton.setForeground(Color.BLACK);
        diaryButton.setFocusable(Boolean.FALSE);
        diaryButton.addActionListener(e -> showDiaryPanel());

        JButton friendsButton = new JButton("Friends");
        friendsButton.setBackground(new Color(128, 128, 128));
        friendsButton.setForeground(Color.BLACK);
        friendsButton.setFocusable(Boolean.FALSE);
        friendsButton.addActionListener(e -> showFriendsPanel());

        JButton activityButton = new JButton("Activity");
        activityButton.setBackground(new Color(128, 128, 128));
        activityButton.setForeground(Color.BLACK);
        activityButton.setFocusable(Boolean.FALSE);
        activityButton.addActionListener(e -> showActivityPanel());

        JButton chatButton = new JButton("Chat");
        chatButton.setBackground(new Color(128, 128, 128));
        chatButton.setForeground(Color.BLACK);
        chatButton.setFocusable(Boolean.FALSE);
        chatButton.addActionListener(e -> showChatPanel());

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(128, 128, 128));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusable(Boolean.FALSE);
        logoutButton.addActionListener(e -> logout());

        toolbarPanel.add(dashboardButton);
        toolbarPanel.add(searchButton);
        toolbarPanel.add(profileButton);
        toolbarPanel.add(diaryButton);
        toolbarPanel.add(activityButton);
        toolbarPanel.add(friendsButton);
        toolbarPanel.add(chatButton);
        toolbarPanel.add(logoutButton);

        return toolbarPanel;
    }

    private void onRegisterSuccess() {
        contentPane.add(toolbar, BorderLayout.NORTH);
        contentPane.revalidate();

        showDashboardPanel();
    }

    public void showDashboardPanel() {
        cardLayout.show(contentArea, "dashboardPanel");
    }

    public void showSearchPanel() {
        cardLayout.show(contentArea, "searchPanel");
    }

    public void showProfilePanel() {
        profilePanel.loadWatchlist();
        profilePanel.loadFriends();
        cardLayout.show(contentArea, "profilePanel");
    }

    public void showDiaryPanel() {
        movieDiaryPanel.loadWatchedMovies();
        cardLayout.show(contentArea, "movieDiaryPanel");
    }

    public void showMovieInfoPanel(MovieDto movieDto) {
        movieInfoPanel.displayMovieInfo(movieDto);
        cardLayout.show(contentArea, "movieInfoPanel");
    }

    public void showFriendsPanel() {
        friendsPanel.loadUsers();
        cardLayout.show(contentArea, "friendsPanel");
    }

    public void showActivityPanel() {
        activityPanel.loadActivities();
        cardLayout.show(contentArea, "activityPanel");
    }

    public void showChatPanel() {
        chatPanel.loadMessages();
        cardLayout.show(contentArea, "chatPanel");
    }

    public void showRegisterPanel() {
        cardLayout.show(contentArea, "registerPanel");

        contentPane.remove(toolbar);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void showLoginPanel() {cardLayout.show(contentArea, "loginPanel");}

    private void logout() {
        loginPanel.clearFields();
        registerPanel.clearFields();
        searchPanel.clearSearchResults();
        contentPane.remove(toolbar);
        showLoginPanel();
    }
}
