package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Watchlist;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class WatchlistDao extends AbstractHibernateDao<Watchlist> {

    public WatchlistDao() {
        setClazz(Watchlist.class);
    }

    public Watchlist getWatchlistByUserIdAndProductId(User user, Product product) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Watchlist> criteria = builder.createQuery(Watchlist.class);
        Root<Watchlist> root = criteria.from(Watchlist.class);
        criteria.select(root).where(builder.and(
                builder.equal(root.get("user"), user),
                builder.equal(root.get("product"), product)
        ));
        return session.createQuery(criteria).uniqueResult();
    }

    public List<Watchlist> getWatchlistByUserId(User user) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Watchlist> criteria = builder.createQuery(Watchlist.class);
        Root<Watchlist> root = criteria.from(Watchlist.class);
        criteria.select(root).where(builder.equal(root.get("user"), user));
        return session.createQuery(criteria).getResultList();
    }

    public void addWatchlist(User user, Product product) {
        if (user == null || product == null) {
            throw new IllegalArgumentException("User or Product cannot be null");
        }

        Session session = sessionFactory.getCurrentSession();

        Watchlist.WatchlistId watchlistId = new Watchlist.WatchlistId();
        watchlistId.setUserId(user.getUserId());
        watchlistId.setProductId(product.getProductId());
        Watchlist watchlist = Watchlist.builder()
                .id(watchlistId)
                .user(user)
                .product(product)
                .build();

        session.save(watchlist);
    }

    public void removeWatchlist(Watchlist watchlist) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(watchlist);
    }
    public Watchlist getWatchlistById(int id) {
        return this.findById(id);
    }

    public List<Watchlist> getAllWatchLists() {
        return this.getAll();
    }


    public List<Watchlist> getAllWatchListsForUser(User user) {
        return getCurrentSession()
                .createQuery("from Watchlist where user = :user", Watchlist.class)
                .setParameter("user", user)
                .getResultList();
    }
}