package com.geumbang.serverb.auth.config;

import com.geumbang.serverb.auth.service.AuthService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Bean
    public Server grpcServer(AuthService authService) {
        return ServerBuilder
                .forPort(50051)  // gRPC 서버 포트 설정
                .addService(authService)  // gRPC 서비스 추가
                .build();
    }
}
