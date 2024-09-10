package com.geumbang.serverb.auth.service;

import com.geumbang.serverb.auth.util.JwtUtil;
import com.geumbang.serverb.grpc.auth.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final JwtUtil jwtUtil;

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
