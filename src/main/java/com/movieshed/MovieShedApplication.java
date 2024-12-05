package com.movieshed;

import com.movieshed.gui.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

@SpringBootApplication
@EntityScan
@Configuration
public class MovieShedApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(MovieShedApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = context.getBean(MainFrame.class);
            mainFrame.setVisible(true);
        });
    }
}

