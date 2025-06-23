package com.hacom.order.controller;

import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{orderId}")
    public Mono<Order> getOrderStatus(@PathVariable String orderId) {
        return repository.findByOrderId(orderId);
    }

    @GetMapping("/count")
    public Flux<Order> getOrdersBetween(@RequestParam String from, @RequestParam String to) {
        return repository.findByTsBetween(from, to);
    }


}
