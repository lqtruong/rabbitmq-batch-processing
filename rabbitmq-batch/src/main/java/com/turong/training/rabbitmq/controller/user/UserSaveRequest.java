package com.turong.training.rabbitmq.controller.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turong.training.rabbitmq.controller.WebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class UserSaveRequest implements WebRequest {

    @Override
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    private Long id;
    private String username;
    private String email;
    private String tenant;

    @JsonIgnore
    public boolean hasUsername() {
        return !StringUtils.isBlank(username);
    }

    @JsonIgnore
    public boolean hasEmail() {
        return !StringUtils.isBlank(email);
    }

    @JsonIgnore
    public boolean hasTenant() {
        return !StringUtils.isBlank(tenant);
    }

    @JsonIgnore
    public boolean valid() {
        return hasUsername() && hasTenant() && hasEmail();
    }

}
