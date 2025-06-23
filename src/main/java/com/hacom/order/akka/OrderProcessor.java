package com.hacom.order.akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import com.hacom.order.grpc.OrderRequest;
import com.hacom.order.metrics.OrderMetricsService;
import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;
import com.hacom.order.smpp.SmppClient;
import lombok.extern.slf4j.Slf4j;
import java.time.OffsetDateTime;

@Slf4j
public class OrderProcessor extends AbstractBehavior<OrderRequest> {

    private final OrderRepository repository;
    private final SmppClient smppService;
    private final OrderMetricsService metricsService;

    public static Behavior<OrderRequest> create(OrderRepository repository, SmppClient smppService, OrderMetricsService metricsService) {
        return Behaviors.setup(context -> new OrderProcessor(context, repository, smppService, metricsService));
    }

    private OrderProcessor(ActorContext<OrderRequest> context,
                           OrderRepository repository,
                           SmppClient smppService,
                           OrderMetricsService metricsService) {
        super(context);
        this.repository = repository;
        this.smppService = smppService;
        this.metricsService = metricsService;
    }

    @Override
    public Receive<OrderRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(OrderRequest.class, this::onInsertOrder)
                .build();
    }

    private Behavior<OrderRequest> onInsertOrder(OrderRequest request) {
        log.info("Recibiendo pedido: {}", request);

        Order order = Order.builder()
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .customerPhoneNumber(request.getCustomerPhoneNumber())
                .items(request.getItemsList())
                .status("RECEIVED")
                .ts(OffsetDateTime.now().toString())
                .build();

        repository.save(order)
                .doOnSuccess(saved -> {
                    log.info("Pedido guardado: {}", saved);
                    metricsService.incrementProcessedOrders();
                })
                .doOnError(error -> log.error("Error al guardar pedido", error))
                .subscribe(savedOrder -> {
                    String message = "Tu pedido " + savedOrder.getOrderId() + " ha sido recibido.";
                    smppService.sendSms(savedOrder.getCustomerPhoneNumber(), message);
                });

        return this;
    }

}
