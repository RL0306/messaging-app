package com.example.message.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Group {
    private Long id;
    private List<User> users;

    public Group(Long id, List<User> users) {
        this.id = id;
        this.users = users;
    }
}
