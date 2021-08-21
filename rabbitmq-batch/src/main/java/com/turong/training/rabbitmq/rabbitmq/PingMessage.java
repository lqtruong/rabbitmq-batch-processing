package com.turong.training.rabbitmq.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turong.training.rabbitmq.entity.Ping;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Slf4j
public class PingMessage {

    private String val;
    private String pinId;

    @JsonIgnore
    public static Ping toPingFromJSON(String message) {
        PingMessage msg = null;
        try {
            msg = new ObjectMapper().readValue(message, PingMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Json processing error", e);
            return null;
        }
        return toPing(msg);
    }

    @JsonIgnore
    public static Ping toPing(PingMessage message) {
        Ping ping = new Ping();
        ping.setVal(message.getVal());
        ping.setPinId(message.getPinId());
        ping.setCreatedAt(new Date());
        ping.setModifiedAt(new Date());
        return ping;
    }

}
