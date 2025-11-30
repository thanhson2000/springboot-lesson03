package com.springbootdemo.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD}) // 調用之處：FIELD-變量，METHOD-方法 等等
@Retention(RetentionPolicy.RUNTIME) // 調用階段：RUNTIME-運行中
@Constraint(validatedBy = {BirthdayValidator.class})  // 指向到代碼處理類{}
public @interface BirthdayAnnotation {
    String message() default "年齡不足";

    int min();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
