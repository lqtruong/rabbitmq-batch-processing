package com.turong.training.rabbitmq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.turong.training.rabbitmq.config.RabbitMQConfig;
import com.turong.training.rabbitmq.entity.Ping;
import com.turong.training.rabbitmq.service.dto.PingResult;
import com.turong.training.rabbitmq.service.ping.PingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // log.info("receivePing, Thread ID: {}", Thread.currentThread().getId());
        // log.info("receivePing channel={}", channel.getChannelNumber());
        // log.info("receivePing messages={}", messages);
        // write to db
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        Map<String, Long> deliveryTags = new HashMap<>();
        final List<Ping> pings = messages.stream()
                .filter(Objects::nonNull)
                .map(m -> {
                    Ping ping = PingMessage.toPingFromJSON(new String(m.getBody(), StandardCharsets.UTF_8));
                    deliveryTags.put(ping.getPinId(), m.getMessageProperties().getDeliveryTag());
                    return ping;
                })
                .collect(Collectors.toList());

        log.info("Write batch pings size ={}", pings.size());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<PingResult> errors = pingService.writeBatch(pings);

        log.info("Errors list size {}", errors.size());

        if (!CollectionUtils.isEmpty(errors)) {
            errors.stream().forEach(e -> {
                String pingId = e.getPinId();
                if (deliveryTags.containsKey(pingId)) {
                    try {
                        log.info("NACK error event {}", pingId);
                        // channel.basicNack(deliveryTags.get(pingId), false, true);
                        channel.basicReject(deliveryTags.get(pingId), false);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        log.info("failed when NACK error event {}", pingId);
                    }
                }
            });
        }

        stopWatch.stop();
        // log.info("writing pings, cost={}ms, pings={}", stopWatch.getTotalTimeMillis(), pings);
    }

}
