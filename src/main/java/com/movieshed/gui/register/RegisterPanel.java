package com.movieshed.gui.register;

import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieShedUserService;
import lombok.Getter;
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
    private JTextField passwordField;
    private JTextField emailField;
    private JButton registerButton;

    @Setter
    private Runnable onRegisterSuccess;

    @Autowired
    private MovieShedUserService userService;

    @Getter
    private MovieShedUser currentUser;

    private GridLayout gridLayout = new GridLayout(4, 2);

    public RegisterPanel() {
        this.setLayout(gridLayout);
        userNameLabel = new JLabel("Username:", JLabel.CENTER);
        passwordLabel = new JLabel("Password:", JLabel.CENTER);
        emailLabel = new JLabel("Email:", JLabel.CENTER);
        userNameField = new JTextField();
        passwordField = new JTextField();
        emailField = new JTextField();
        registerButton = new JButton("Register");

        add(userNameLabel);
        add(userNameField);
        add(passwordLabel);
        add(passwordField);
        add(emailLabel);
        add(emailField);
        add(registerButton);

        registerButton.addActionListener(e -> handleRegisterButtonClick());
    }

    public void handleRegisterButtonClick() {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        MovieShedUser user = userService.createMovieShedUser(userName, password, email);
        log.info("User successfully saved: {}", user);

        onRegisterSuccess.run();
    }

    public String getUserName() {
        return userNameField.getText();
    }

}
