package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.OrderItemDao;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    private OrderItemDao orderItemDao;

    @Autowired
    public void setOrderItemDao(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @Transactional
    public List<OrderItem> getAllOrderItems() {
        return orderItemDao.getAllOrderItems();
    }

    @Transactional
    public OrderItem getOrderItemById(int id) {
        return orderItemDao.getOrderItemById(id);
    }

    @Transactional
    public void addOrderItem(OrderItem orderItem) {
        orderItemDao.addOrderItem(orderItem);
    }

}