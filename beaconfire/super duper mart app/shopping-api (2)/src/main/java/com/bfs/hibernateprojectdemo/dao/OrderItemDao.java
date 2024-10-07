package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDao extends AbstractHibernateDao<OrderItem> {

    public OrderItemDao() {
        setClazz(OrderItem.class);
    }

    public OrderItem getOrderItemById(int id) {
        return this.findById(id);
    }

    public List<OrderItem> getAllOrderItems() {
        return this.getAll();
    }

    public void addOrderItem(OrderItem orderItem) {
        this.add(orderItem);
    }

}