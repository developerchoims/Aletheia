package com.geumbang.servera.auth.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String userId;
    private String password;
}
