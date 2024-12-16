package com.movieshed.impl;

import com.movieshed.UserContext;
import com.movieshed.model.Message;
import com.movieshed.model.MovieShedUser;
import com.movieshed.repository.MessageRepository;
import com.movieshed.repository.MovieShedUserRepository;
import com.movieshed.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MovieShedUserRepository movieShedUserRepository;

    @Override
    public List<Message> getMessages() {
        return messageRepository.findAllByOrderByCreatedAtAsc();
    }

    @Override
    public Message sendMessage(UUID senderId, String content) {

        MovieShedUser sender = movieShedUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setCreatedAt(Instant.now());

        System.out.println("Sending Message: " + content);
        System.out.println("Message content: " + message.getContent());


        return messageRepository.save(message);
    }
}
