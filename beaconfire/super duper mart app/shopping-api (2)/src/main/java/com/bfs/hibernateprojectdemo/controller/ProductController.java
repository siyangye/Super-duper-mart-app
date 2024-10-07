package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.dto.product.ProductRequest;
import com.bfs.hibernateprojectdemo.domain.vo.*;
import com.bfs.hibernateprojectdemo.service.OrderService;
import com.bfs.hibernateprojectdemo.service.ProductService;
import com.bfs.hibernateprojectdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private OrderService orderService;

    private UserService userService;

    public ProductController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAvailableProducts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() == 1) {
            return ResponseEntity.ok(productService.getAllAvailableProducts());
        }
        return ResponseEntity.ok(productService.getAllAvailableProductsForUser());
    }

    @GetMapping("/{id}")
    public DataResponse getProductById(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        ProductVO productVO = productService.getProductById(id);
        if (user.getRole() == 1) {
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .data(productVO)
                    .build();
        }
        ProductUserVO productUserVO = new ProductUserVO();
        productUserVO.setProductId(productVO.getProductId());
        productUserVO.setName(productVO.getName());
        productUserVO.setDescription(productVO.getDescription());
        productUserVO.setRetailPrice(productVO.getRetailPrice());
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(productUserVO)
                .build();
    }

    @PostMapping
    public Object addProduct(@RequestBody ProductRequest productRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() != 1) {
            return DataResponse.builder()
                    .success(false)
                    .message("Fail")
                    .data("You do not have permission to view this page, please contact the administrator.")
                    .build();
        }
        try {
            Product product = productService.addProduct(productRequest);
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public Object updateProduct(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() != 1) {
            return DataResponse.builder()
                    .success(false)
                    .message("Fail")
                    .data("You do not have permission to view this page, please contact the administrator.")
                    .build();
        }
        try {
            Product updatedProduct = productService.updateProduct(id, productRequest);
            if (updatedProduct != null) {
                return DataResponse.builder()
                        .success(true)
                        .message("Success")
                        .build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/frequent/{limit}")
    public ResponseEntity<?> getTopFrequentOrdersByUser(@PathVariable int limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        List<OrderVO> orders = orderService.getAllOrdersByUser(user);

        Map<Integer, Long> productCount = orders.stream()
                .flatMap(order -> order.getOrderItems().stream()
                        .map(OrderItemVO::getProductId))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Integer> topProductIds = productCount.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<ProductUserVO> result = productService.getProductByIds(topProductIds);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<ProductUserVO>> getRecentOrdersByUser(@PathVariable int limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        List<OrderVO> orders = orderService.getAllOrdersByUser(user);

        orders.sort(Comparator.comparing(OrderVO::getDatePlaced).reversed());

        List<OrderVO> recentOrders = orders.stream()
                .limit(limit)
                .collect(Collectors.toList());

        List<Integer> productIds = recentOrders.stream()
                .flatMap(order -> order.getOrderItems().stream()
                        .map(OrderItemVO::getProductId))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());

        List<ProductUserVO> result = productService.getProductBySortedIds(new ArrayList<>(productIds));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/profit/{limit}")
    public Object getTopMostProfitableProducts(@PathVariable int limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() != 1) {
            return DataResponse.builder()
                    .success(false)
                    .message("Fail")
                    .data("You do not have permission to view this page, please contact the administrator.")
                    .build();
        }

        if (limit <= 0) {
            return ResponseEntity.badRequest().body("Limit must be a positive number.");
        }
        List<Product> topProfitableProducts = productService.getMostProfitableProduct(limit);
        return ResponseEntity.ok(topProfitableProducts);
    }

    @GetMapping("/popular/{limit}")
    public Object getTopPopularProducts(@PathVariable int limit) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() != 1) {
            return DataResponse.builder()
                    .success(false)
                    .message("Fail")
                    .data("You do not have permission to view this page, please contact the administrator.")
                    .build();
        }

        List<ProductTopVO> topPopularProducts = orderService.getTopPopularProducts(limit);
        return ResponseEntity.ok(topPopularProducts);
    }

    @GetMapping("/success")
    public Object getTotalSuccessfulSales() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() != 1) {
            return DataResponse.builder()
                    .success(false)
                    .message("Fail")
                    .data("You do not have permission to view this page, please contact the administrator.")
                    .build();
        }

        int totalSuccessfulSales = orderService.getTotalSuccessfulSales();
        return ResponseEntity.status(200).body(totalSuccessfulSales);
    }
}