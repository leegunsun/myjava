package com.example.testspringboot.demo;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidator.class) // Validator 클래스 연결
@Target({ ElementType.METHOD, ElementType.FIELD }) // 적용 가능한 위치
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점까지 유지
public @interface CustomAnnotation {

    String message() default "유효하지 않은 값입니다."; // 기본 메시지

    Class<?>[] groups() default {}; // 검증 그룹

    Class<? extends Payload>[] payload() default {}; // 추가 메타데이터
}