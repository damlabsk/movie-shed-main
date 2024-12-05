package com.movieshed.gui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);
    }
}
