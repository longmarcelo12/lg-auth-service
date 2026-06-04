package com.example.lgauthservice.shared.utils.trim_text;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class TrimTextBinder {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomStringTrimmerEditor customStringEditor = new CustomStringTrimmerEditor();
        binder.registerCustomEditor(String.class, customStringEditor);
    }
}
