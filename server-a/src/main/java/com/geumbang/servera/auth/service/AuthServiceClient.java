package com.geumbang.servera.auth.service;

import com.geumbang.servera.grpc.auth.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Tag(name = "AuthServiceClient", description = "AuthServiceClient에 대한 설명입니다.")
public class AuthServiceClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public AuthServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051) // Server B 주소
                .usePlaintext()
                .build();
        this.authStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    @Operation(summary = "토큰 발급", description = "server-b에 토큰 발급을 요청합니다.")
    @Parameter(name = "userId", description = "유저 아이디")
    @Parameter(name = "password", description = "유저 비밀번호")
    public TokenResponse generateToken(String userId, String role) {
        LoginRequest request = LoginRequest.newBuilder()
                .setUserId(userId)
                .setRole(role)
                .build();

        return authStub.generateToken(request);
    }

    @Operation(summary = "액세스 토큰 유효성 검사", description = "server-b에 액세스 토큰 유효성 검사를 요청합니다.")
    @Parameter(name = "acsToken", description = "액세스 토큰")
    public ValidateTokenResponse validateAcsToken(String acsToken) {
        TokenRequest request = TokenRequest.newBuilder()
                .setAccessToken(acsToken)
                .build();

        return authStub.validateAcsToken(request);
    }

    @Operation(summary = "리프레시 토큰 유효성 검사", description = "server-b에 리프레시 토큰 유효성 검사를 요청합니다.")
    @Parameter(name = "rfrToken", description = "리프레시 토큰")
    public ValidateTokenResponse validateRfrToken(String rfrToken) {
        TokenRequest request = TokenRequest.newBuilder()
                .setRefreshToken(rfrToken)
                .build();

        return authStub.validateRfrToken(request);
    }


}
