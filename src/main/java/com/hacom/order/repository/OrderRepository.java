package com.hacom.order.repository;

import com.hacom.order.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
    Mono<Order> findByOrderId(String orderId);
    Flux<Order> findByTsBetween(String  from, String  to);

}
