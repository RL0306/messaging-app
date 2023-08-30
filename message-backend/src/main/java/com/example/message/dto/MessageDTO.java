package com.example.message.dto;

import com.example.message.model.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    //user id it is coming from
    private Long from;
    private String message;
    //user id or group id it is going to
    private Long to;
    private LocalDateTime timeOfMessage;
    @JsonIgnore
    private boolean read;
    private MessageType type;
}
