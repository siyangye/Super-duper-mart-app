package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.User;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDao extends AbstractHibernateDao<Order> {

    public OrderDao() {
        setClazz(Order.class);
    }

    public Order getOrderById(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> orderRoot = criteria.from(Order.class);
        criteria.select(orderRoot).where(builder.equal(orderRoot.get("orderId"), id));
        return session.createQuery(criteria).uniqueResult();
    }

    public List<Order> getAllOrders() {
        return this.getAll();
    }

    public void createOrder(Order order) {
        sessionFactory.getCurrentSession().save(order);
    }

    public void updateOrder(Order order) {
        sessionFactory.getCurrentSession().update(order);
    }
    public void addOrder(Order order) {
        this.add(order);
    }

    public List<Order> getAllOrdersByUser(User user) {
        return getCurrentSession()
                .createQuery("from Order where user = :user", Order.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Order> getTopFrequentOrdersForAdmin(User user, int limit) {
        return getCurrentSession()
                .createQuery("from Order where user = :user group by product_id order by count(*) desc", Order.class)
                .setParameter("user", user)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> getRecentOrdersForAdmin(User user, int limit) {
        return getCurrentSession()
                .createQuery("from Order where user = :user order by datePlaced desc", Order.class)
                .setParameter("user", user)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> orderRoot = criteria.from(Order.class);
        criteria.select(orderRoot);
        return session.createQuery(criteria).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
    }

    public List<Order> getCompletedOrders() {
        return getCurrentSession()
                .createQuery("from Order where orderStatus = :statusCompleted", Order.class)
                .setParameter("statusCompleted", "Completed")
                .getResultList();
    }
}