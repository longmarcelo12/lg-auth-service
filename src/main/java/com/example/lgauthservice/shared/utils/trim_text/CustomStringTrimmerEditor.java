package com.example.lgauthservice.shared.utils.trim_text;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CustomStringTrimmerEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.toString() : "");
    }

    @Override
    public void setAsText(String text) {
        if (text == null) {
            setValue(null);
        } else {
            setValue(StringUtils.stripToEmpty(URLDecoder.decode(text, StandardCharsets.UTF_8)));
        }
    }
}
