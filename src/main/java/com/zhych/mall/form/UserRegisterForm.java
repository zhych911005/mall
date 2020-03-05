package com.zhych.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {

    //@NotEmpty用于集合
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}