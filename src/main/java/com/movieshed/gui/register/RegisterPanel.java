package com.movieshed.gui.register;

import com.movieshed.UserContext;
import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieShedUserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Slf4j
@Setter
public class RegisterPanel extends JPanel {
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    public JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerButton;

    @Setter
    private Runnable onRegisterSuccess;

    @Autowired
    private MovieShedUserService userService;

    @Autowired
    private MovieInfoPanel moviePanel;

    public RegisterPanel() {
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        int panelWidth = 800;
        int panelHeight = 600;

        int formWidth = 300;
        int formHeight = 250;
        int startX = (panelWidth - formWidth) / 2;
        int startY = (panelHeight - formHeight) / 2;

        JLabel ms = new JLabel("<html><span style='color:white;'>Welcome to movie</span><span style='color:red;'>SHED</span></html>");
        ms.setFont(new Font("Arial", Font.PLAIN, 24));
        ms.setHorizontalAlignment(SwingConstants.CENTER);
        ms.setBounds(startX, startY - 100, formWidth, 34);
        this.add(ms);

        JLabel titleLabel = new JLabel("LOG IN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(startX, startY - 40, formWidth, 30);
        this.add(titleLabel);

        userNameLabel = createStyledLabel("Username");
        userNameLabel.setBounds(startX, startY, formWidth, 20);
        userNameField = new JTextField();
        userNameField.setBounds(startX, startY + 25, formWidth, 20);
        this.add(userNameLabel);
        this.add(userNameField);

        passwordLabel = createStyledLabel("Password:");
        passwordLabel.setBounds(startX, startY + 65, formWidth, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(startX, startY + 90, formWidth, 20);
        this.add(passwordLabel);
        this.add(passwordField);

        emailLabel = createStyledLabel("Email:");
        emailLabel.setBounds(startX, startY + 130, formWidth, 20);
        emailField = new JTextField();
        emailField.setBounds(startX, startY + 155, formWidth, 20);
        this.add(emailLabel);
        this.add(emailField);

        registerButton = new JButton("Register");
        registerButton.setBounds(startX + (formWidth - 100) / 2, startY + 190, 100, 30);
        registerButton.setBackground(Color.LIGHT_GRAY);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        this.add(registerButton);

        registerButton.addActionListener(e -> handleRegisterButtonClick());
    }

    public void handleRegisterButtonClick() {
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MovieShedUser user = userService.createMovieShedUser(userName, password, email);
            UserContext.setUser(user);

            moviePanel.resetAddMovieButton();

            if (onRegisterSuccess != null) {
                onRegisterSuccess.run();
            }
        } catch (Exception ex) {
            log.error("Failed to register user", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Failed to log in. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }

    public String getUserName() {
        return userNameField.getText();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }
}
