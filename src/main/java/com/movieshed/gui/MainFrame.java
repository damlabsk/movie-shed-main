package com.movieshed.gui;

import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.gui.profile.ProfilePanel;
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
    private ProfilePanel profilePanel;

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

        contentArea.add(registerPanel, "registerPanel");
        contentArea.add(dashboardPanel, "dashboardPanel");
        contentArea.add(searchPanel, "searchPanel");
        contentArea.add(profilePanel, "profilePanel");
        contentArea.add(movieInfoPanel, "movieInfoPanel");

        registerPanel.setOnRegisterSuccess(this::onRegisterSuccess);

        contentPane.add(contentArea, BorderLayout.CENTER);

        setContentPane(contentPane);
    }

    private JPanel createToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.BLACK);

        JButton dashboardButton = new JButton("Dashboard");
        dashboardButton.setBackground(new Color(128, 128, 128));
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setFocusable(Boolean.FALSE);
        dashboardButton.addActionListener(e -> showDashboardPanel());

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(128, 128, 128));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusable(Boolean.FALSE);
        searchButton.addActionListener(e -> showSearchPanel());

        JButton profileButton = new JButton("Profile");
        profileButton.setBackground(new Color(128, 128, 128));
        profileButton.setForeground(Color.WHITE);
        profileButton.setFocusable(Boolean.FALSE);
        profileButton.addActionListener(e -> showProfilePanel());

        JButton friendsButton = new JButton("Friends");
        friendsButton.setBackground(new Color(128, 128, 128));
        friendsButton.setForeground(Color.WHITE);
        friendsButton.setFocusable(Boolean.FALSE);
        friendsButton.addActionListener(e -> showFriendsPanel());

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(128, 128, 128));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(Boolean.FALSE);
        logoutButton.addActionListener(e -> logout());

        toolbarPanel.add(dashboardButton);
        toolbarPanel.add(searchButton);
        toolbarPanel.add(profileButton);
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
        cardLayout.show(contentArea, "profilePanel");
    }

    public void showMovieInfoPanel(MovieDto movieDto) {
        movieInfoPanel.displayMovieInfo(movieDto);
        cardLayout.show(contentArea, "movieInfoPanel");
    }

    public void showFriendsPanel() {}

    public void showRegisterPanel() {
        cardLayout.show(contentArea, "registerPanel");

        contentPane.remove(toolbar);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void logout() {
        registerPanel.clearFields();
        showRegisterPanel();
    }
}
