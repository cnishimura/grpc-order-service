package com.hacom.order.grpc;

import com.hacom.order.grpc.proto.OrderServiceImpl;
import com.hacom.order.metrics.OrderMetricsService;
import com.hacom.order.repository.OrderRepository;
import com.hacom.order.smpp.SmppClient;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GrpcServer {

    Server server;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SmppClient smppClient;

    @Autowired
    OrderMetricsService metricsService;

    @PostConstruct
    public void start() throws IOException {
        this.server = ServerBuilder
                .forPort(9090)
                .addService(new OrderServiceImpl(orderRepository, smppClient, metricsService))
                .build()
                .start();

        log.info("✅ gRPC Server started on port 9090");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("🔴 Shutting down gRPC server...");
            GrpcServer.this.stop();
        }));
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            log.info("🛑 gRPC Server stopped.");
        }
    }
}
