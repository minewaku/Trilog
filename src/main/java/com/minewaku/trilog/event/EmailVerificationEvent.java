package com.minewaku.trilog.event;

import org.springframework.context.ApplicationEvent;

import com.minewaku.trilog.entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailVerificationEvent extends ApplicationEvent {

    private final User user;

    public EmailVerificationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}