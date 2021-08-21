package com.turong.training.rabbitmq.controller;

public interface WebValidator<WebRequest> {

    boolean validate(final WebRequest request);

}
