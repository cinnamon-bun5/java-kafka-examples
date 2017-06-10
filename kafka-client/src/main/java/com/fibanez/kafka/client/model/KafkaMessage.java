package com.fibanez.kafka.client.model;

import java.io.Serializable;

/**
 * Created by fibanez on 10/6/17.
 */
public class KafkaMessage implements Serializable {

    Long messageId;

    String message;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                '}';
    }
}
