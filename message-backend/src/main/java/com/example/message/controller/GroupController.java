package com.example.message.controller;

import com.amazonaws.Response;
import com.example.message.dto.GroupSubscribeDTO;
import com.example.message.dto.MessageDTO;
import com.example.message.service.GroupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/group")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessageToGroup(@RequestBody MessageDTO incomingMessage) throws JsonProcessingException {
        Map<String, String> response = groupService.sendMessageToGroup(incomingMessage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<GroupSubscribeDTO>> getGroupsIdForUser(@PathVariable Long userId){
        List<GroupSubscribeDTO> groupIds = groupService.getGroupIdsForUser(userId);
        return ResponseEntity.ok(groupIds);
    }
}
