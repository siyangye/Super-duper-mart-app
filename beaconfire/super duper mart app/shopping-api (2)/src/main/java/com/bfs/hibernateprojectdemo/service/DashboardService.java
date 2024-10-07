package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.OrderDao;
import com.bfs.hibernateprojectdemo.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DashboardService {
    private final OrderDao orderDao;

    @Autowired
    public DashboardService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
    public List<Order> getOrdersPage(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return orderDao.findAll(pageable);
    }
}