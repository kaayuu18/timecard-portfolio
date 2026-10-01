package com.example.timecard.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

@Documented//なんかapiドキュメントに乗るらしい必須ではない
@Constraint(validatedBy = PersonValidator.class)//検証先
@Target({ElementType.FIELD,ElementType.METHOD})//バリデーションが実行されるメソッドと設定しているフィールド
@Retention(RetentionPolicy.RUNTIME)//保持期間(実行されている間)
@ReportAsSingleViolation//アノテーションをまとめるアノテーション
public @interface PersonV {
         String message() default "存在しません";
         Class<?>[] groups() default {};
         Class<? extends Payload>[] payload() default {};
         
} 
