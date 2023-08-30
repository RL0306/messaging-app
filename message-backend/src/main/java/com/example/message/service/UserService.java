package com.example.message.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.message.dto.MessageDTO;
import com.example.message.model.MessageType;
import com.example.message.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.message.util.Data.users;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final AwsService awsService;
    //hardcoded users

    public Map<String, String> sendMessageToUser(MessageDTO message) throws JsonProcessingException {

        //check if the message is coming from a valid user
        users.stream()
                .filter(e -> e.getId().equals(message.getFrom())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with user id:" + message.getFrom()));

        //find the user who the message went to
        User userTo = users.stream()
                .filter(e -> e.getId().equals(message.getTo())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with user id:" + message.getTo()));


        message.setTimeOfMessage(LocalDateTime.now());
        message.setType(MessageType.PRIVATE);
        userTo.getMessages().add(message);

        awsService.sendMessageToAWSQueue(message);

        return Collections.singletonMap("status", "message successfully sent");
    }

    public List<MessageDTO> readMessagesForUser(Long userIdMessageToRead){
        log.info("reading data for {} at time {}", userIdMessageToRead, LocalDateTime.now());
        User user = users.stream()
                .filter(e -> e.getId().equals(userIdMessageToRead)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with user id:" + userIdMessageToRead));


        //get all the messages and set read as true as this is the one we are returning to user as this is unseen
        List<MessageDTO> messagesNotSeenForUser = user.getMessages()
                .stream()
                .filter(e -> !e.isRead())
                .collect(Collectors.toList());

        //we will update all the unread ones to read as those are the ones being sent inside the request
        user.getMessages().forEach(e -> e.setRead(true));

        return messagesNotSeenForUser;
    }


    public static List<User> getUsers() {
        return users;
    }
}
