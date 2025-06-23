package com.hacom.order.actor;

import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class OrderProcessorActorTest {

    @Test
    public void testActorProcessesOrder() {
        OrderRepository repository = mock(OrderRepository.class);
        when(repository.save(any(Order.class))).thenReturn(Mono.just(new Order()));

        OrderProcessorActor actor = new OrderProcessorActor(repository);
        Order order = new Order();
        order.setOrderId("1");
        order.setCustomerId("C1");
        order.setCustomerPhoneNumber("123456");
        order.setItems(Collections.singletonList("Item1"));
        order.setStatus("NEW");
        order.setTs(OffsetDateTime.now());

        actor.createReceive().onMessage().apply(order);
        verify(repository, times(1)).save(any(Order.class));
    }
}
