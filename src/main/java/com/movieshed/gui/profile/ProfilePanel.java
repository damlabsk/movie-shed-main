package com.movieshed.gui.profile;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ProfilePanel extends JPanel {

    public ProfilePanel() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Profile Panel - User Information");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
