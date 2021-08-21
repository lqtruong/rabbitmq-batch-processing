package com.turong.training.rabbitmq.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {

    private String username;
    private String email;
    private String tenant;

}
