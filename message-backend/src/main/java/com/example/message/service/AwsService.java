package com.example.message.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.message.dto.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AwsService {

    private static final String QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/759921279107/MyQueue";
    @Autowired
    private ObjectMapper mapper;
    private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    public void sendMessageToAWSQueue(MessageDTO message) throws JsonProcessingException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(QUEUE_URL)
                .withMessageBody(mapper.writeValueAsString(message));
        sqs.sendMessage(sendMessageRequest);
    }
}
