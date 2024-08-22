package com.monetize360.learning_spring_core.example1;

import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public String getMessage() {
        return "Hello,SpringBoot";
    }
}

