package com.turong.training.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${spring.kafka.bootstrapServers:}")
    private String bootstrapServers;

    @Value("${spring.rabbitmq.listener.simple.concurrency:}")
    private Integer concurrentConsumers;

    private static Integer MAX_PRIORITY = 100;
    private static String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    private static String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    private static String PRIORITY = "x-max-priority";

    public static final String PING_EXCHANGE = "rabbitmq.pingExchange";
    public static final String PING_QUEUE = "rabbitmq.pingQueue";
    public static final String PING_ROUTING_KEY = "rabbitmq.pingRoutingKey";

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(50);
        executor.setCorePoolSize(20);
        executor.setThreadNamePrefix("ping-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor,
            ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(PING_EXCHANGE);
        rabbitTemplate.setRoutingKey(PING_ROUTING_KEY);
        rabbitTemplate.setDefaultReceiveQueue(PING_QUEUE);
        rabbitTemplate.setTaskExecutor(taskExecutor);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange pingExchange() {
        return new DirectExchange(PING_EXCHANGE);
    }

    @Bean
    public Queue pingQueue() {
        Map<String, Object> args = new HashMap<>(16);
        args.put(DEAD_LETTER_QUEUE_KEY, PING_EXCHANGE);
        args.put(DEAD_LETTER_ROUTING_KEY, PING_ROUTING_KEY);
        args.put(PRIORITY, MAX_PRIORITY);
        return new Queue(PING_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding pingBinding() {
        return BindingBuilder.bind(pingQueue()).to(pingExchange()).with(PING_ROUTING_KEY);
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setBatchListener(true);
        factory.setBatchSize(200);
        factory.setConsumerBatchEnabled(true); // enable consumer batch
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
