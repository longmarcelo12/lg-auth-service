package com.example.lgauthservice.shared.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtils {

    private final MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode, Object... params) {
        String message = Utilities.returnNullInException(() -> messageSource.getMessage(messageCode, params, Locale.getDefault()));
        return Utilities.getOrDefault(message, messageCode);
    }
}