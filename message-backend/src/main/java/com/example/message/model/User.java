package com.example.message.model;

import com.example.message.dto.MessageDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private List<MessageDTO> messages;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.messages = new ArrayList<>();
    }
}
