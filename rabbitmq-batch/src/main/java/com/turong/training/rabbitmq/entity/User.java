package com.turong.training.rabbitmq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import java.time.LocalDate;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("users")
@Accessors(chain = true)
public class User {

    @Id
    private Long id;
    private String username;
    private String email;
    private String tenant;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

}
