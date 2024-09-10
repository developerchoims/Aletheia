package com.geumbang.serverb;

import io.grpc.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerRunner implements CommandLineRunner {

    private final Server grpcServer;

    public GrpcServerRunner(Server grpcServer) {
        this.grpcServer = grpcServer;
    }

    @Override
    public void run(String... args) throws Exception {
        grpcServer.start();  // gRPC 서버 시작
        grpcServer.awaitTermination();  // 서버가 종료되지 않도록 유지
    }
}
