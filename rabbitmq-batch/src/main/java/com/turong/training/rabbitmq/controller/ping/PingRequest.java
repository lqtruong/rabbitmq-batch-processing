package com.turong.training.rabbitmq.controller.ping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turong.training.rabbitmq.controller.WebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PingRequest implements WebRequest {

    @Override
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    private int times;
    private String value;

}
