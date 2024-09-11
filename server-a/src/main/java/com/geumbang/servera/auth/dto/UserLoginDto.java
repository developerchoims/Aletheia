package com.geumbang.servera.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginDto {
    @Schema(name = "id", description = "user id", type = "UUID")
    private String userId;
    @Schema(name = "userPwd", description = "로그인 시 사용될 Pwd")
    private String password;
}
