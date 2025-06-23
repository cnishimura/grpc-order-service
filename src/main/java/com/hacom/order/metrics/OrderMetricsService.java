package com.hacom.order.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Service;

@Service
public class OrderMetricsService {

    private final Counter orderProcessedCounter;

    public OrderMetricsService(MeterRegistry meterRegistry) {
        this.orderProcessedCounter = meterRegistry.counter("order.processed.count");
    }

    public void incrementProcessedOrders() {
        orderProcessedCounter.increment();
    }
}
