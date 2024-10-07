package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import com.bfs.hibernateprojectdemo.domain.dto.orderitem.OrderItemCreationRequest;
import com.bfs.hibernateprojectdemo.service.OrderItemService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/order-item")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/all")
    public DataResponse getAllOrderItems() {
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(orderItemService.getAllOrderItems())
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getOrderItemById(@PathVariable int id) {
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(orderItemService.getOrderItemById(id))
                .build();
    }

    @PostMapping
    public DataResponse addOrderItem(@Valid @RequestBody OrderItemCreationRequest request, BindingResult result) {
        if (result.hasErrors()) return DataResponse.builder()
                                            .success(false)
                                            .message("Something went wrong")
                                            .build();

        OrderItem orderItem = OrderItem.builder()
                .order(request.getOrder())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .purchasedPrice(request.getPurchasedPrice())
                .build();

        orderItemService.addOrderItem(orderItem);

        return DataResponse.builder()
                .success(true)
                .message("Success")
                .build();
    }
}