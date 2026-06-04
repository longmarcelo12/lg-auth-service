package com.example.lgauthservice.shared.annotation.validator;

import com.example.lgauthservice.shared.annotation.Annotations;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class VnPhoneNumberValidator implements ConstraintValidator<Annotations.VnPhoneNumber, String> {

    private static final String REGEX =
            "^(0|\\+84|84)(96|97|98|86|32|33|34|35|36|37|38|39|91|94|81|82|83|84|85|88|89|90|93|70|79|77|76|78|92|56|58|99|59|87|55|52)\\d{7}$";
    private String message;

    @Override
    public void initialize(Annotations.VnPhoneNumber constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return true;

        boolean isValid = value.matches(REGEX);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }
}