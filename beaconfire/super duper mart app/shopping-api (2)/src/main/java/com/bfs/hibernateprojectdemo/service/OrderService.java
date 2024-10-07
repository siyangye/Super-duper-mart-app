package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.OrderDao;
import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.dto.order.OrderRequest;
import com.bfs.hibernateprojectdemo.domain.vo.OrderItemVO;
import com.bfs.hibernateprojectdemo.domain.vo.OrderVO;
import com.bfs.hibernateprojectdemo.domain.vo.ProductTopVO;
import com.bfs.hibernateprojectdemo.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderDao orderDao;

    private ProductService productService;

    private UserService userService;

    private OrderItemService orderItemService;

    private final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1L, 1L);

    @Autowired
    public void setOrderDao(OrderDao orderDao, ProductService productService, UserService userService, OrderItemService orderItemService) {
        this.orderDao = orderDao;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.userService = userService;
    }

    @Transactional
    public List<OrderVO> getAllOrders() {
        return orderDao.getAllOrders().stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(order.getOrderId());
            orderVO.setOrderStatus(order.getOrderStatus());
            orderVO.setUserId(order.getUser().getUserId());
            orderVO.setProductId(order.getProduct().getProductId());
            orderVO.setDatePlaced(order.getDatePlaced());
            orderVO.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(orderItem.getItem_id());
                orderItemVO.setProductId(orderItem.getProduct().getProductId());
                orderItemVO.setQuantity(orderItem.getQuantity());
                orderItemVO.setPurchasedPrice(orderItem.getPurchasedPrice());
                return orderItemVO;
            }).collect(Collectors.toList()));
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<OrderVO> getAllOrdersByUser(User user) {
        return orderDao.getAllOrdersByUser(user).stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(order.getOrderId());
            orderVO.setOrderStatus(order.getOrderStatus());
            orderVO.setUserId(order.getUser().getUserId());
            orderVO.setProductId(order.getProduct().getProductId());
            orderVO.setDatePlaced(order.getDatePlaced());
            orderVO.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(orderItem.getItem_id());
                orderItemVO.setProductId(orderItem.getProduct().getProductId());
                orderItemVO.setQuantity(orderItem.getQuantity());
                orderItemVO.setPurchasedPrice(orderItem.getPurchasedPrice());
                return orderItemVO;
            }).collect(Collectors.toList()));
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Order getOrderById(int id) {
        return orderDao.getOrderById(id);
    }

    @Transactional
    public List<Order> getTopFrequentOrdersForUser(User user, int limit) {
        return orderDao.getTopFrequentOrdersForAdmin(user, limit);
    }

    @Transactional
    public List<?> getRecentOrdersForUser(User user, int limit) {
        if (user.getRole() == 0) {
            return orderDao.getRecentOrdersForAdmin(user, limit)
                    .stream()
                    .map(getOrderOrderVO())
                    .collect(Collectors.toList());
        }
        return orderDao.getRecentOrdersForAdmin(user, limit);
    }

    private static Function<Order, OrderVO> getOrderOrderVO() {
        return order -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(order.getOrderId());
            orderVO.setOrderStatus(order.getOrderStatus());
            orderVO.setUserId(order.getUser().getUserId());
            orderVO.setProductId(order.getProduct().getProductId());
            orderVO.setDatePlaced(order.getDatePlaced());
            return orderVO;
        };
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);

        Order order = new Order();
        Integer orderId = (int) (idGenerator.nextId() & 0xFFFFFFF);
        order.setOrderId(orderId);
        order.setOrderStatus("Processing");
        order.setUser(user);
        order.setProduct(Product.builder().productId(orderRequest.getOrder().get(0).getProductId()).build());
        order.setDatePlaced(new Date());
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.Item item : orderRequest.getOrder()) {
            Product product = productService.purchaseProduct(item.getProductId(), item.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPurchasedPrice(product.getRetailPrice());
            orderItems.add(orderItem);
        }
        orderDao.createOrder(order);
        for (OrderItem orderItem : orderItems) {
            orderItemService.addOrderItem(orderItem);
        }
        return order;
    }

    @Transactional
    public Order cancelOrder(Order order) {
        if (!order.getOrderStatus().equals("Processing")) {
            throw new IllegalStateException("Cannot cancel order that is not in Processing status");
        }
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            if (product == null) {
                continue;
            }
            int quantity = item.getQuantity();
            productService.cancelOrder(product.getProductId(), quantity);
        }
        order.setOrderStatus("Canceled");
        orderDao.updateOrder(order);
        return order;
    }

    @Transactional
    public void addOrder(Order... orders) {
        for (Order o : orders) {
            orderDao.addOrder(o);
        }
    }

    @Transactional
    public Order updateOrderStatus(int orderId, String status, Long userId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null || !Objects.equals(order.getUser().getUserId(), userId)) {
            return null;
        }

        if (!"Processing".equals(order.getOrderStatus()) && "complete".equals(status)) {
            throw new IllegalStateException("Order cannot be completed unless it's processing.");
        }

        if ("cancel".equals(status) && !"Processing".equals(order.getOrderStatus())) {
            throw new IllegalStateException("Order cannot be canceled unless it's processing.");
        }

        if ("cancel".equals(status)) {
            return cancelOrder(order);
        } else if ("complete".equals(status)) {
            order.setOrderStatus("Completed");
        } else if ("processing".equals(status)) {
            order.setOrderStatus("Processing");
        }

        orderDao.updateOrder(order);
        return order;
    }

    @Transactional
    public int getTotalSuccessfulSales() {
        List<Order> completedOrders = orderDao.getCompletedOrders();
        int totalSales = 0;
        for (Order order : completedOrders) {
            for (OrderItem item : order.getOrderItems()) {
                totalSales += item.getQuantity();
            }
        }
        return totalSales;
    }

    @Transactional
    public List<ProductTopVO> getTopPopularProducts(int limit) {
        List<Order> allOrders = orderDao.getAllOrders().stream().filter(order -> !Objects.equals(order.getOrderStatus(), "Completed")).collect(Collectors.toList());
        List<OrderItem> allOrderItems = new ArrayList<>();
        for (Order o : allOrders) {
            allOrderItems.addAll(o.getOrderItems());
        }
        Map<Product, Integer> popularMap = allOrderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)));
        return popularMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(productIntegerEntry -> {
                    Product product = productIntegerEntry.getKey();
                    ProductTopVO productTopVO = new ProductTopVO();
                    productTopVO.setProductId(product.getProductId());
                    productTopVO.setName(product.getName());
                    productTopVO.setRetailPrice(product.getRetailPrice());
                    productTopVO.setWholesalePrice(product.getWholesalePrice());
                    productTopVO.setDescription(productTopVO.getDescription());
                    productTopVO.setCount(productIntegerEntry.getValue());
                    return productTopVO;
                })
                .collect(Collectors.toList());
    }
}