package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.dto.order.OrderRequest;
import com.bfs.hibernateprojectdemo.exception.NotEnoughInventoryException;
import com.bfs.hibernateprojectdemo.service.OrderService;
import com.bfs.hibernateprojectdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public DataResponse getAllOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        HashMap<String, Object> map = new HashMap<>();

        if (user.getRole() == 1) {
            map.put("orders", orderService.getAllOrders());
        } else {
            map.put("orders", orderService.getAllOrdersByUser(user));
        }
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(map)
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getOrderById(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        Order order = orderService.getOrderById(id);
        if (order == null || !order.getUser().getUserId().equals(user.getUserId())) {
            return DataResponse.builder()
                 .success(false)
                 .message("Order not found")
                 .build();
        }
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(order)
                .build();
    }


    @PostMapping
    public Object createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.createOrder(orderRequest);
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .build();
        } catch (NotEnoughInventoryException e) {
            return DataResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PatchMapping("/{orderId}/{status}")
    public Object updateOrderStatus(@PathVariable int orderId, @PathVariable String status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status, user.getUserId());
            if (updatedOrder != null) {
                return DataResponse.builder()
                        .success(true)
                        .message("Success")
                        .build();
            } else {
                return DataResponse.builder()
                        .success(false)
                        .message("Order not found")
                        .build();
            }
        } catch (IllegalStateException ex) {
            return DataResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build();
        }
    }

}