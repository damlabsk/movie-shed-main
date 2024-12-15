package com.movieshed.gui.register;

import com.movieshed.gui.MainFrame;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.movieshed.gui.movieInfo.MovieInfoPanel;
import com.movieshed.service.MovieShedUserService;
import org.springframework.beans.factory.annotation.Autowired;

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
    private Runnable onLoginSuccess;

    @Lazy
    @Autowired
    private MainFrame mainFrame;

    public LoginPanel() {

        int panelWidth = 800;
        int panelHeight = 600;

        int formWidth = 300;
        int formHeight = 300;
        int startX = (panelWidth - formWidth) / 2;
        int startY = (panelHeight - formHeight) / 2;

        this.setBackground(Color.BLACK);
        this.setLayout(null);

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
        userNameField.setBackground(Color.WHITE);
        userNameField.setForeground(Color.BLACK);
        this.add(userNameLabel);
        this.add(userNameField);

        passwordLabel = createStyledLabel("Password:");
        passwordLabel.setBounds(startX, startY + 65, formWidth, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(startX, startY + 90, formWidth, 20);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        this.add(passwordLabel);
        this.add(passwordField);

        emailLabel = createStyledLabel("Email:");
        emailLabel.setBounds(startX, startY + 130, formWidth, 20);
        emailField = new JTextField();
        emailField.setBounds(startX, startY + 155, formWidth, 20);
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.BLACK);
        this.add(emailLabel);
        this.add(emailField);

        loginButton = new JButton("LOG IN");
        loginButton.setBounds(startX + (formWidth - 100) / 2, startY + 190, 100, 30);
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        this.add(loginButton);

        loginButton.addActionListener(e -> handleLoginButtonClick());

        JLabel createAccountLabel = new JLabel("<html><span style='color:white;'>Don't have an account? <u>Create one here</u></span></html>");
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        createAccountLabel.setForeground(Color.WHITE);
        createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createAccountLabel.setBounds(startX, startY + 170, formWidth, 30);

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

        this.add(createAccountLabel);
    }

    private void redirectToRegister() {
        RegisterPanel registerPanel = new RegisterPanel();
        registerPanel.setVisible(true);
    }

    public void handleLoginButtonClick() {
        String userName = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        //Buradan sonrasında spring hatası veriyor daha çözemedim ne olduğunu o yüzden main frame'e eklemedim

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
