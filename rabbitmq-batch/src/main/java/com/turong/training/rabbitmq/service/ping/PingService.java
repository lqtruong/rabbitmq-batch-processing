package com.turong.training.rabbitmq.service.ping;

import com.turong.training.rabbitmq.entity.Ping;

import java.util.List;

public interface PingService {

    void writeBatch(List<Ping> pings);


}
