package com.turong.training.rabbitmq.service.ping;

import com.turong.training.rabbitmq.entity.Ping;
import com.turong.training.rabbitmq.mapper.PingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PingServiceImpl implements PingService {

    @Autowired
    private PingMapper pingMapper;

    @Override
    public void writeBatch(List<Ping> pings) {
        // log.info("Write to db={}", pings);
        // pings.stream().forEach(ping -> pingMapper.insert(ping));
        int total = pingMapper.writeBatch(pings);
        log.info("total inserted={}", total);
    }
}
