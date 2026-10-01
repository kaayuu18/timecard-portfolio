package com.example.timecard;

// 従業員登録フォームの入力値と、必須項目などの入力ルールをまとめる。

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePerson {
    @NotBlank(message = "名前を入力してください")
    private String name;
    @Min(0)
    @Max(100)
    @NotNull(message="年齢を入力してください")
    private Integer age;
    @NotBlank(message="電話番号を入力してください")
    private String phoneNum;
    @Email
    @NotBlank(message = "メールアドレスを入力してください")
    private String mail;
    @NotBlank(message = "住所を入力してください")
    private String address;
    @NotNull(message = "必須です")
    private String password;
    @NotNull(message = "必須")
    private String role;

}
