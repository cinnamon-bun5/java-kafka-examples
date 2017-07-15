package com.fibanez.kafka.delayedClient.model;

import java.io.Serializable;

/**
 * Created by fibanez on 10/6/17.
 */
public class KafkaDelayedMessage implements Serializable {

    Long messageId;

    String message;

    Long delayTo;

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

    public Long getDelayTo() {
        return delayTo;
    }

    public void setDelayTo(Long delayTo) {
        this.delayTo = delayTo;
    }

    @Override
    public String toString() {
        return "KafkaDelayedMessage{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                ", delayTo=" + delayTo +
                '}';
    }
}
