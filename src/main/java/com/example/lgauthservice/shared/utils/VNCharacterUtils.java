package com.example.lgauthservice.shared.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class VNCharacterUtils {
    //thêm 2 list hoặc set tìm index để replace

    private static final Pattern DIACRITICS_PATTERN =
            Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String removeAccent(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccent = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");

        // xử lý riêng cho đ / Đ
        return withoutAccent
                .replace('đ', 'd')
                .replace('Đ', 'D');
    }
}
