package com.example.message.service;

import com.example.message.dto.GroupSubscribeDTO;
import com.example.message.dto.MessageDTO;
import com.example.message.model.Group;
import com.example.message.model.MessageType;
import com.example.message.util.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.message.util.Data.groups;


@Service
@AllArgsConstructor
public class GroupService {

    private final AwsService awsService;

    public Map<String, String> sendMessageToGroup(MessageDTO incomingMessage) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        //get the group who the message is going to
        Long groupIdTo = incomingMessage.getTo();

        //find the group from the list
        Group group = groups.stream()
                .filter(e -> e.getId().equals(groupIdTo))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cannot find group with id of " + groupIdTo));

        //get all the users and put the new message into their messages list (this is where we store all their messages)
        group.getUsers()
                .forEach(e -> {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setMessage(incomingMessage.getMessage());
                    messageDTO.setFrom(incomingMessage.getFrom());
                    messageDTO.setTimeOfMessage(now);
                    messageDTO.setTo(e.getId());
                    messageDTO.setType(MessageType.GROUP);
                    e.getMessages().add(messageDTO);
                });

        awsService.sendMessageToAWSQueue(incomingMessage);

        return Collections.singletonMap("status", "message successfully sent");

    }

    public List<GroupSubscribeDTO> getGroupIdsForUser(Long userId) {
        return groups.stream()
                .filter(group -> group.getUsers()
                        .stream().anyMatch(user -> user.getId().equals(userId)))
                .map(e -> new GroupSubscribeDTO(e.getId())).collect(Collectors.toList());
    }

}
