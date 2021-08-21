package com.turong.training.rabbitmq.rabbitmq;

import com.turong.training.rabbitmq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@Slf4j
public class PingSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(int times, String val) {

        IntStream.range(0, times).forEach(i -> {
            // log.info("Send message ith={}", i);
            PingMessage body = new PingMessage();
            body.setVal(val);
            body.setPinId("" + i);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PING_EXCHANGE,
                    RabbitMQConfig.PING_ROUTING_KEY, body);
        });

    }

}
