package com.springbootdemo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordAnnotation, String> {

    private Pattern digitPattern;
    private Pattern letterPattern;
    private Pattern specialPattern;

    private final int MIN_PASS = 8;
    private final int MAX_PASS = 16;

    @Override
    public void initialize(PasswordAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        digitPattern = Pattern.compile(".*[0-9].*");
        letterPattern = Pattern.compile(".*[a-zA-Z].*");
        specialPattern = Pattern.compile(".*[@#$%^&+=!*()_\\-{}\\[\\]:;\"'<>,.?/~`|].*");
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        password = password.trim();

        // 檢查密碼長度
        if (password.length() < MIN_PASS || password.length()> MAX_PASS){
            // 禁用默認消息
            context.disableDefaultConstraintViolation();

            // 自定義消息，添加消息庫
            context.buildConstraintViolationWithTemplate("密碼長度需要在8～16個字符").addConstraintViolation();

            return false;
        }

        // 檢查是否包含數字
        if (! digitPattern.matcher(password).matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("密碼需要包含至少一個數字").addConstraintViolation();
            return false;
        }

        // 檢查是否包含英文字母
        if (! letterPattern.matcher(password).matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("密碼需要包含至少一個英文字母").addConstraintViolation();
            return false;
        }

        // 檢查是否包含特殊符號
        if(! specialPattern.matcher(password).matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("密碼需要包含至少一個特殊符號").addConstraintViolation();
            return false;
        }
        return true;
    }
}
