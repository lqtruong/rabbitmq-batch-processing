package com.turong.training.rabbitmq.service.ping;

import com.turong.training.rabbitmq.entity.Ping;
import com.turong.training.rabbitmq.mapper.PingMapper;
import com.turong.training.rabbitmq.service.dto.PingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PingServiceImpl implements PingService {

    @Autowired
    private PingMapper pingMapper;

    @Override
    public List<PingResult> writeBatch(List<Ping> pings) {
        // log.info("Write to db={}", pings);
        // pings.stream().forEach(ping -> pingMapper.insert(ping));

        int newSize = pings.size() / 3;
        Collections.shuffle(pings);

        List<PingResult> errors = pings.subList(0, newSize).stream().map(PingResult::fromPing).collect(Collectors.toList());

        int total = pingMapper.writeBatch(pings.subList(newSize, pings.size()));
        log.info("total inserted={}", total);

        return errors;
    }
}
