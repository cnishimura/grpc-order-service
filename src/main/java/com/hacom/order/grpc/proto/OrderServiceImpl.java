package com.hacom.order.grpc.proto;


import com.hacom.order.akka.OrderProcessor;
import com.hacom.order.metrics.OrderMetricsService;
import com.hacom.order.repository.OrderRepository;
import com.hacom.order.smpp.SmppClient;
import io.grpc.stub.StreamObserver;
import com.hacom.order.grpc.OrderRequest;
import com.hacom.order.grpc.OrderResponse;
import com.hacom.order.grpc.OrderServiceGrpc;
import akka.actor.typed.ActorSystem;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private final ActorSystem<OrderRequest> actorSystem;
    private final OrderMetricsService metricsService;


    public OrderServiceImpl(OrderRepository repository, SmppClient smppClient, OrderMetricsService metricsService) {
        this.metricsService = metricsService; // ← Necesario
        this.actorSystem = ActorSystem.create(
                OrderProcessor.create(repository, smppClient, metricsService),
                "orderProcessorSystem"
        );
    }

    @Override
    public void insertOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        actorSystem.tell(request);
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(request.getOrderId())
                .setStatus("RECEIVED")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

