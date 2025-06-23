package com.hacom.order.actor;

import akka.actor.AbstractActor;
import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;

import java.time.OffsetDateTime;


public class OrderProcessorActor extends AbstractActor {

    private final OrderRepository orderRepository;

    public OrderProcessorActor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Order.class, order -> {
                    order.setStatus("PROCESSED");
                    order.setTs(OffsetDateTime.now().toString());
                    orderRepository.save(order).subscribe();
                    // send SMS here
                })
                .build();
    }
}
