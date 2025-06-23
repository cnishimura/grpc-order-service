package com.hacom.order.grpc;

import com.hacom.order.grpc.proto.OrderServiceImpl;
import com.hacom.order.metrics.OrderMetricsService;
import com.hacom.order.model.Order;
import com.hacom.order.repository.OrderRepository;
import com.hacom.order.smpp.SmppClient;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Test
    public void testInsertOrder() {
        // Mocks de dependencias
        OrderRepository repository = mock(OrderRepository.class);
        SmppClient smppClient = mock(SmppClient.class);
        OrderMetricsService metricsService = mock(OrderMetricsService.class);

        // Mock de repositorio: simula guardado de pedido
        when(repository.save(any(Order.class))).thenReturn(Mono.just(new Order()));

        // Servicio a testear
        OrderServiceImpl service = new OrderServiceImpl(repository, smppClient, metricsService);

        // Construcción del request
        OrderRequest request = OrderRequest.newBuilder()
                .setOrderId("1")
                .setCustomerId("C123")
                .setCustomerPhoneNumber("987654321")
                .addItems("Item1")
                .build();

        // Observer simulado
        @SuppressWarnings("unchecked")
        StreamObserver<OrderResponse> observer = mock(StreamObserver.class);

        // Ejecución
        service.insertOrder(request, observer);

        // Verificaciones
        verify(observer, times(1)).onNext(any(OrderResponse.class));
        verify(observer, times(1)).onCompleted();
    }
}
