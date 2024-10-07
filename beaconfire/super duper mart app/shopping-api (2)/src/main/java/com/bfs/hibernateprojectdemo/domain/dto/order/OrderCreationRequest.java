package com.bfs.hibernateprojectdemo.domain.dto.order;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Product;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {

    @NotNull(message = "User cannot be null")
    User user;

    @NotNull(message = "Product cannot be null")
    Product product;

    @NotNull(message = "Order status cannot be null")
    String orderStatus;

    @NotNull(message = "Date placed cannot be null")
    Date datePlaced;
}