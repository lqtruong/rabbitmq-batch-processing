package com.turong.training.rabbitmq.controller.user;

import com.turong.training.rabbitmq.controller.WebValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserValidator implements WebValidator<UserSaveRequest> {

    @Override
    public boolean validate(final UserSaveRequest request) {
        if (!request.valid()) {
            log.debug("Validated request={}", request);
            throw new IllegalArgumentException("The request is not valid.");
        }
        return true;
    }
}
