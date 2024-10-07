package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.WatchlistDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Watchlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistService {
    private WatchlistDao watchlistDao;

    @Autowired
    public void setWatchlistDao(WatchlistDao watchlistDao) {
        this.watchlistDao = watchlistDao;
    }

    @Transactional
    public List<Watchlist> getAllWatchLists() {
        return watchlistDao.getAllWatchLists();
    }

    @Transactional
    public Watchlist getWatchlistById(int id) {
        return watchlistDao.getWatchlistById(id);
    }

    @Transactional
    public boolean addProductToWatchlist(User user, Product product) {
        Watchlist existingWatchlist = watchlistDao.getWatchlistByUserIdAndProductId(user, product);
        if (existingWatchlist == null) {
            watchlistDao.addWatchlist(user, product);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeProductFromWatchlist(User user, Product product) {
        Watchlist existingWatchlist = watchlistDao.getWatchlistByUserIdAndProductId(user, product);
        if (existingWatchlist != null) {
            watchlistDao.removeWatchlist(existingWatchlist);
            return true;
        }
        return false;
    }

    @Transactional
    public List<Product> getAvailableWatchlistProducts(User user) {
        List<Watchlist> watchlists = watchlistDao.getWatchlistByUserId(user);
        List<Product> products = new ArrayList<>();
        for (Watchlist watchlist : watchlists) {
            Product product = watchlist.getProduct();
            if (product.getQuantity() > 0) {
                products.add(product);
            }
        }
        return products;
    }

    @Transactional
    public List<Watchlist> getAllWatchListsForUser(User user) {
        return watchlistDao.getAllWatchListsForUser(user);
    }
}