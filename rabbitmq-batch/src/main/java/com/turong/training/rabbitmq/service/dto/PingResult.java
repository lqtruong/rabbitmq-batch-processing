package com.turong.training.rabbitmq.service.dto;

import com.turong.training.rabbitmq.entity.Ping;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PingResult {

    Long id;
    String pinId;
    boolean success;

    public static PingResult fromPing(Ping ping) {
        PingResult result = new PingResult();
        result.setId(ping.getId());
        result.setPinId(ping.getPinId());
        result.setSuccess(false);
        return result;
    }

}
