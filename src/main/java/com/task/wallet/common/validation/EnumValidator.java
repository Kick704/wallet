package com.task.wallet.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Валидатор перечислений (enum), проверяющий, что строка соответствует одному из значений перечисления
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        allowedValues = Arrays.stream(constraintAnnotation.clazz().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || allowedValues.contains(s);
    }
}
