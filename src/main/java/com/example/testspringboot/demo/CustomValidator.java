package com.example.testspringboot.demo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomValidator implements ConstraintValidator<CustomAnnotation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 검증 로직 작성
        if (value == null || value.trim().isEmpty()) {
            setCustomMessage(context, "값이 비어있습니다."); // 경우 1: 값이 비어있음
            return false; // null 또는 공백 문자열은 유효하지 않음
        }

        if (!value.matches("^[a-zA-Z]+$")) {
            setCustomMessage(context, "값은 알파벳만 포함해야 합니다."); // 경우 2: 알파벳 이외의 값
            return false;
        }

        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
        context.buildConstraintViolationWithTemplate(message) // 사용자 정의 메시지 설정
                .addConstraintViolation();
    }
}