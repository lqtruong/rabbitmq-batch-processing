package com.turong.training.rabbitmq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turong.training.rabbitmq.entity.Ping;

import java.util.List;

public interface PingMapper extends BaseMapper<Ping> {

    int writeBatch(List<Ping> pings);

}
