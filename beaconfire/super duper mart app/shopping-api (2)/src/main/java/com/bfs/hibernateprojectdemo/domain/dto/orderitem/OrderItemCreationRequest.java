package com.bfs.hibernateprojectdemo.domain.dto.orderitem;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.Product;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreationRequest {

    @NotNull(message = "Order cannot be null")
    Order order;

    @NotNull(message = "Product cannot be null")
    Product product;

    @NotNull(message = "Quantity cannot be null")
    Integer quantity;

    @NotNull(message = "Purchased price cannot be null")
    Double purchasedPrice;
}