package com.movieshed.gui.login;

import com.movieshed.UserContext;
import com.movieshed.gui.MainFrame;
import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MovieShedUserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
@Slf4j
@Setter
public class LoginPanel extends JPanel {

    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    public JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton loginButton;

    @Autowired
    private MovieShedUserService userService;

    @Autowired
    private MovieInfoPanel moviePanel;

    @Setter
    private Runnable onRegisterSuccess;

    @Lazy
    @Autowired
    private MainFrame mainFrame;

    public LoginPanel() {

        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel ms = new JLabel("<html><span style='color:white;'>Welcome to movie</span><span style='color:red;'>SHED</span></html>");
        ms.setFont(new Font("Arial", Font.PLAIN, 24));
        ms.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(ms, gbc);

        JLabel titleLabel = new JLabel("LOG IN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        this.add(titleLabel, gbc);

        userNameLabel = createStyledLabel("Username:");
        gbc.gridy = 2;
        this.add(userNameLabel, gbc);

        userNameField = new JTextField();
        userNameField.setBackground(Color.WHITE);
        userNameField.setForeground(Color.BLACK);
        gbc.gridy = 3;
        this.add(userNameField, gbc);

        passwordLabel = createStyledLabel("Password:");
        gbc.gridy = 4;
        this.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        gbc.gridy = 5;
        this.add(passwordField, gbc);

        emailLabel = createStyledLabel("Email:");
        gbc.gridy = 6;
        this.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.BLACK);
        gbc.gridy = 7;
        this.add(emailField, gbc);

        loginButton = new JButton("LOG IN");
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 8;
        this.add(loginButton, gbc);

        loginButton.addActionListener(e -> handleLoginButtonClick());

        JLabel createAccountLabel = new JLabel("<html><span style='color:white;'>Don't have an account? <u>Create one here</u></span></html>");
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        createAccountLabel.setForeground(Color.WHITE);
        createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToRegister();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                createAccountLabel.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                createAccountLabel.setForeground(Color.WHITE);
            }
        });
        gbc.gridy = 9;
        this.add(createAccountLabel, gbc);
    }

    private void redirectToRegister() {
        if (mainFrame != null) {
            mainFrame.showRegisterPanel();
        } else {
            log.error("MainFrame is not set. Cannot redirect to RegisterPanel.");
        }
    }

    public void handleLoginButtonClick() {
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MovieShedUser user = userService.validateUser(userName, password, email);

            if (user != null) {
                UserContext.setUser(user);
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (onRegisterSuccess != null) {
                    onRegisterSuccess.run();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            log.error("Failed to log in user", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Failed to log in. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    public void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }
}
