package com.task.wallet.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации значений, принадлежащих определенному перечислению (enum)
 * @see EnumValidator
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    Class<? extends Enum<?>> clazz();
    String message() default "Недопустимое значение";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
