package com.geumbang.serverb.auth.service;

import com.geumbang.serverb.auth.util.JwtUtil;
import com.geumbang.serverb.grpc.auth.*;
import io.grpc.stub.StreamObserver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "AuthService", description = "JWT Token 발급 및 인증이 이루어집니다.")
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final JwtUtil jwtUtil;

    @Operation(summary = "generateToken", description = "토큰을 발급합니다.")
    @Parameter(name = "userId", description = "유저 아이디")
    @Parameter(name = "role", description = "유저 권한")
    @ApiResponse(responseCode = "200", description = "TokenResponse")
    @Override
    public void generateToken(LoginRequest request, StreamObserver<TokenResponse> responseObserver){
        String userId = request.getUserId();
        String role = request.getRole();
        String accessToken = jwtUtil.generateAcsToken(userId, role);
        String refreshToken = jwtUtil.generateRfrToken(userId, role);

        TokenResponse response = TokenResponse.newBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Operation(summary = "validateAcsToken", description = "액세스 토큰 유효성 검사를 합니다.")
    @Parameter(name = "accessToken", description = "액세스토큰")
    @ApiResponse(responseCode = "200", description = "ValidateTokenResponse")
    @Override
    public void validateAcsToken(TokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        String accessToken = request.getAccessToken();
        boolean isValid = jwtUtil.isValidToken(accessToken);
        String userId = "";
        String role = "";

        if (isValid) {
            userId = jwtUtil.getIdFromToken(accessToken);
            role = jwtUtil.getRoleFromToken(accessToken);
        }

        ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                .setIsValid(isValid)
                .setUserId(userId)
                .setRole(role)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Operation(summary = "validateRfrToken", description = "리프레쉬 토큰 유효성 검사를 합니다.")
    @Parameter(name = "refreshToken", description = "리프레쉬토큰")
    @ApiResponse(responseCode = "200", description = "ValidateTokenResponse")
    @Override
    public void validateRfrToken(TokenRequest request, StreamObserver<ValidateTokenResponse> responseObserver) {
        String refreshToken = request.getRefreshToken();
        boolean isValid = jwtUtil.isValidToken(refreshToken);
        String userId = "";
        String role = "";

        if (isValid) {
            userId = jwtUtil.getIdFromToken(refreshToken);
            role = jwtUtil.getRoleFromToken(refreshToken);
        }

        if(userId.isEmpty() || role.isEmpty()){
            isValid = false;
        }

        ValidateTokenResponse response = ValidateTokenResponse.newBuilder()
                .setIsValid(isValid)
                .setUserId(userId)
                .setRole(role)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
