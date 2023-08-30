package com.example.message.controller;

import com.example.message.dto.MessageDTO;
import com.example.message.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessageToUser(@RequestBody MessageDTO message) throws JsonProcessingException {
        Map<String, String> response = userService.sendMessageToUser(message);
        return ResponseEntity.ok(response);
    }

    /**
     * Get message for particular user with id
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<MessageDTO>> readMessagesForUser(@PathVariable("id") Long userIdMessageToRead){
        List<MessageDTO> messages = userService.readMessagesForUser(userIdMessageToRead);
        return ResponseEntity.ok(messages);
    }
}
