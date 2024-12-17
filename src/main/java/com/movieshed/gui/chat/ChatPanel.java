package com.movieshed.gui.chat;

import com.movieshed.UserContext;
import com.movieshed.model.Message;
import com.movieshed.model.MovieShedUser;
import com.movieshed.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ChatPanel extends JPanel {

    @Autowired
    private MessageService messageService;

    private JPanel chatEntriesPanel;
    private JTextField inputField;
    private JScrollPane chatScrollPane;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel chatLabel = new JLabel("<html><span style='color:white;'>Global </span><span style='color:red;'>Chat</span></html>");
        chatLabel.setFont(new Font("Arial", Font.BOLD, 20));
        chatLabel.setForeground(Color.WHITE);
        chatLabel.setHorizontalAlignment(SwingConstants.CENTER);

        chatEntriesPanel = new JPanel();
        chatEntriesPanel.setLayout(new BoxLayout(chatEntriesPanel, BoxLayout.Y_AXIS));
        chatEntriesPanel.setBackground(Color.BLACK);

        chatScrollPane = new JScrollPane(chatEntriesPanel);
        chatScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.getVerticalScrollBar().setBlockIncrement(16);
        chatScrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        add(chatLabel, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);

        add(createInputPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10,10,10,10));
        inputPanel.setLayout(new BorderLayout(0,2));
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        inputField = new JTextField();
        inputField.setBackground(Color.WHITE);
        inputField.setForeground(Color.BLACK);
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String content = inputField.getText();

                    if (content.isEmpty()) {
                        return;
                    }

                    inputField.setText("");

                    sendMessage(content);
                }
            }
        });
        inputField.setBorder(Utilities.addPadding(0,10,0, 10));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 30));

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Utilities.PRIMARY_COLOR);
        sendButton.setForeground(Utilities.TEXT_COLOR);
        sendButton.setFocusable(false);
        sendButton.setSize(40, 30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sendButton) {
                    String content = inputField.getText();

                    if (content.isEmpty()) {return;}

                    inputField.setText("");
                    sendMessage(content);
                }
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        return inputPanel;
    }

    public void loadMessages() {
        List<Message> messages = messageService.getMessages();
        chatEntriesPanel.removeAll();

        messages.forEach(message -> {
            JPanel messageComponent = createMessageComponent(message);
            chatEntriesPanel.add(messageComponent);
            chatEntriesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        });

        chatEntriesPanel.revalidate();
        chatEntriesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = chatScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

    private void sendMessage(String content) {
        MovieShedUser sender = UserContext.getUser();

        messageService.sendMessage(sender.getId(), content);

        loadMessages();
    }

    private JPanel createMessageComponent(Message message) {
        JPanel messageComponent = new JPanel();
        messageComponent.setBackground(Utilities.TRANSPARENT_COLOR);
        messageComponent.setLayout(new BoxLayout(messageComponent, BoxLayout.Y_AXIS));
        messageComponent.setBorder(Utilities.addPadding(20,20,5,20));

        JLabel usernameLabel = new JLabel(message.getSender().getUserName());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        messageComponent.add(usernameLabel);

        JLabel messageLabel = new JLabel("<html>" + message.getContent() + "</html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);
        messageComponent.add(messageLabel);

        Date createdAtDate = Date.from(message.getCreatedAt());

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTimestamp = dateFormat.format(createdAtDate);

        JLabel timestampLabel = new JLabel(formattedTimestamp);
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        timestampLabel.setForeground(Color.LIGHT_GRAY);
        messageComponent.add(timestampLabel);

        return messageComponent;
    }



}
