package com.turong.training.rabbitmq.convert;

import com.turong.training.rabbitmq.controller.user.UserSaveRequest;
import com.turong.training.rabbitmq.controller.user.UserResponse;
import com.turong.training.rabbitmq.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConvert {

    User toUser(final UserSaveRequest userSaveRequest);

    UserResponse toResponse(final User user);

}
