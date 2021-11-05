package com.turong.training.rabbitmq.service.ping;

import com.turong.training.rabbitmq.entity.Ping;
import com.turong.training.rabbitmq.service.dto.PingResult;

import java.util.List;

public interface PingService {

    List<PingResult> writeBatch(List<Ping> pings);


}
