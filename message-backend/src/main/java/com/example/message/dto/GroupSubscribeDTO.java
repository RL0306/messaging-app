package com.example.message.dto;

import lombok.Data;

@Data
public class GroupSubscribeDTO {
    private Long groupId;

    public GroupSubscribeDTO(Long groupId) {
        this.groupId = groupId;
    }
}
