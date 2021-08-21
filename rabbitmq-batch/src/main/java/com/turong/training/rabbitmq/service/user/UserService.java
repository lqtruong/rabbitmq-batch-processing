package com.turong.training.rabbitmq.service.user;

import com.turong.training.rabbitmq.entity.User;

public interface UserService {

    User getUser(String id);

    User create(User userToCreate);

    int deleteByEmail(String email);

    int deleteById(String id);

}
