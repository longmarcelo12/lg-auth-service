package com.example.lgauthservice.shared.annotation;

import com.example.lgauthservice.shared.annotation.validator.EmailValidator;
import com.example.lgauthservice.shared.annotation.validator.VnPhoneNumberValidator;
import com.example.lgauthservice.shared.constants.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

public interface Annotations {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {EmailValidator.class})
    @Documented
    @interface Email {
        String message() default "invalid email address";

        int size() default Constants.EMAIL_MAX_LENGTH_DEFAULT;

        String regex() default "";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {VnPhoneNumberValidator.class})
    @Documented
    public @interface VnPhoneNumber {
        String message() default "Số điện thoại không tồn tại";

        String regex() default "";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ColumnMapping {
        String name();
    }
}