package com.movieshed.service;

import com.movieshed.model.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> getMessages();

    Message sendMessage(UUID senderId, String content);
}