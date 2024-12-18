package com.movieshed.gui.register;

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

@Component
@Slf4j
@Setter
public class RegisterPanel extends JPanel {
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel cPasswordLabel;
    private JLabel emailLabel;
    public JTextField userNameField;
    private JPasswordField passwordField;
    private JPasswordField cPasswordField;
    private JTextField emailField;
    private JButton registerButton;

    @Setter
    private Runnable onRegisterSuccess;

    @Autowired
    private MovieShedUserService userService;

    @Autowired
    private MovieInfoPanel moviePanel;

    @Lazy
    @Autowired
    private MainFrame mainFrame;

    public RegisterPanel() {
            this.setBackground(Color.BLACK);
            this.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            JLabel ms = new JLabel("<html><span style='color:white;'>Join movie</span><span style='color:red;'>SHED</span></html>");
            ms.setFont(new Font("Arial", Font.PLAIN, 24));
            ms.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridwidth = 2;
            this.add(ms, gbc);

            gbc.gridy++;
            JLabel titleLabel = new JLabel("REGISTER");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.WEST;

            JLabel userNameLabel = createStyledLabel("Username:");
            this.add(userNameLabel, gbc);

            gbc.gridy++;
            userNameField = new JTextField(20);
            userNameField.setBackground(Color.WHITE);
            userNameField.setForeground(Color.BLACK);
            gbc.gridwidth = 2;
            this.add(userNameField, gbc);

            gbc.gridy++;
            JLabel passwordLabel = createStyledLabel("Password:");
            this.add(passwordLabel, gbc);

            gbc.gridy++;
            passwordField = new JPasswordField(20);
            passwordField.setBackground(Color.WHITE);
            passwordField.setForeground(Color.BLACK);
            gbc.gridwidth = 2;
            this.add(passwordField, gbc);

            gbc.gridy++;
            JLabel confirmPasswordLabel = createStyledLabel("Confirm Password:");
            this.add(confirmPasswordLabel, gbc);

            gbc.gridy++;
            cPasswordField = new JPasswordField(20);
            cPasswordField.setBackground(Color.WHITE);
            cPasswordField.setForeground(Color.BLACK);
            gbc.gridwidth = 2;
            this.add(cPasswordField, gbc);

            gbc.gridy++;
            JLabel emailLabel = createStyledLabel("Email:");
            this.add(emailLabel, gbc);

            gbc.gridy++;
            emailField = new JTextField(20);
            emailField.setBackground(Color.WHITE);
            emailField.setForeground(Color.BLACK);
            gbc.gridwidth = 2;
            this.add(emailField, gbc);

            gbc.gridy++;
            gbc.anchor = GridBagConstraints.CENTER;

            registerButton = new JButton("Register");
            registerButton.setBackground(Color.LIGHT_GRAY);
            registerButton.setForeground(Color.BLACK);
            registerButton.setFocusPainted(false);
            registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
            gbc.gridwidth = 2;
            this.add(registerButton, gbc);

            registerButton.addActionListener(e -> handleRegisterButtonClick());
        }


        public void handleRegisterButtonClick() {
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String cPassword = new String(cPasswordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if(!password.equals(cPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MovieShedUser user = userService.createMovieShedUser(userName, password, email);

            if (mainFrame != null) {
                mainFrame.showLoginPanel();
            }

        } catch (Exception ex) {
            log.error("Failed to register user", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Failed to register. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
        cPasswordField.setText("");
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
