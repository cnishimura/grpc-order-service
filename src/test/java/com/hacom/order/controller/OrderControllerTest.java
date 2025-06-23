package com.hacom.order.controller;

import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Test
    public void testGetOrderStatus() {
        OrderRepository mockRepo = mock(OrderRepository.class);
        OrderController controller = new OrderController(mockRepo);

        Order mockOrder = new Order();
        mockOrder.setOrderId("123");
        when(mockRepo.findByOrderId("123")).thenReturn(Mono.just(mockOrder));

        Mono<Order> result = controller.getOrderStatus("123");
        assertEquals("123", result.block().getOrderId());
    }

    @Test
    public void testGetOrdersBetween() {
        OrderRepository mockRepo = mock(OrderRepository.class);
        OrderController controller = new OrderController(mockRepo);

        OffsetDateTime now = OffsetDateTime.now();
        Order order1 = new Order();
        order1.setTs(now.minusDays(1).toString());
        Order order2 = new Order();
        order2.setTs(now.plusDays(1).toString());

        when(mockRepo.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(order1, order2)));

        Flux<Order> results = controller.getOrdersBetween(now.minusDays(2).toString(), now.toString());
        assertEquals(1, results.count().block());
    }
}
