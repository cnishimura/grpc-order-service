package com.hacom.order.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Document(collection = "order")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;

    private String orderId;
    private String customerId;
    private String customerPhoneNumber;
    private List<String> items;
    private String status;
    private String ts;
}
