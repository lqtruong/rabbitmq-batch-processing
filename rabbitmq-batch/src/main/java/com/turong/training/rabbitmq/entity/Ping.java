package com.turong.training.rabbitmq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("pings")
@Accessors(chain = true)
public class Ping {

    @Id
    private Long id;
    private String val;
    private String pinId;
    private Date createdAt;
    private Date modifiedAt;

}
