package com.geumbang.servera.auth.service;

import com.geumbang.servera.grpc.auth.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public AuthServiceClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051) // Server B 주소
                .usePlaintext()
                .build();
        this.authStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public TokenResponse generateToken(String userId, String role) {
        LoginRequest request = LoginRequest.newBuilder()
                .setUserId(userId)
                .setRole(role)
                .build();

        return authStub.generateToken(request);
    }

    public ValidateTokenResponse validateAcsToken(String acsToken) {
        TokenRequest request = TokenRequest.newBuilder()
                .setAccessToken(acsToken)
                .build();

        return authStub.validateAcsToken(request);
    }

    public ValidateTokenResponse validateRfrToken(String rfrToken) {
        TokenRequest request = TokenRequest.newBuilder()
                .setRefreshToken(rfrToken)
                .build();

        return authStub.validateRfrToken(request);
    }


}
