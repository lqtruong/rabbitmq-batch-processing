package com.turong.training.rabbitmq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.turong.training.rabbitmq.config.RabbitMQConfig;
import com.turong.training.rabbitmq.entity.Ping;
import com.turong.training.rabbitmq.service.ping.PingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PingReceiver {

    @Autowired
    private PingService pingService;

    @RabbitListener(
            queues = RabbitMQConfig.PING_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void receivePing(List<Message> messages, Channel channel) {
        log.info("receivePing, Thread ID: {}", Thread.currentThread().getId());
        log.info("receivePing channel={}", channel.getChannelNumber());
        log.info("receivePing messages={}", messages);
        // write to db
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        final List<String> nonNullMessages = messages.stream()
                .filter(Objects::nonNull)
                .map(m -> new String(m.getBody(), StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        List<Ping> pings = nonNullMessages.stream()
                .map(PingMessage::toPingFromJSON).collect(Collectors.toList());
        // log.info("Write batch pings={}", pings);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        pingService.writeBatch(pings);
        stopWatch.stop();
        log.info("writing pings, cost={}ms, pings={}", stopWatch.getTotalTimeMillis(), pings);
    }

}
