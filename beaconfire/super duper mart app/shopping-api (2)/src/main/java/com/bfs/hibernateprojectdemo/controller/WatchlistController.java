package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Watchlist;
import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;

import com.bfs.hibernateprojectdemo.service.UserService;
import com.bfs.hibernateprojectdemo.service.WatchlistService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;

    private final UserService userService;

    public WatchlistController(WatchlistService watchlistService, UserService userService) {
        this.watchlistService = watchlistService;
        this.userService = userService;
    }

    @GetMapping("/products/all")
    public DataResponse getAllWatchlists() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        if (user.getRole() == 1) {
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .data(watchlistService.getAllWatchLists())
                    .build();
        }
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(watchlistService.getAllWatchListsForUser(user))
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getWatchlistById(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Watchlist watchlist = watchlistService.getWatchlistById(id);
        if (user.getRole() != 1 && !Objects.equals(watchlist.getUser().getUserId(), user.getUserId())) {
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .data(new ArrayList<>())
                    .build();
        }
        return DataResponse.builder()
                .success(true)
                .message("Success")
                .data(watchlistService.getWatchlistById(id))
                .build();
    }

    @PostMapping("/product/{id}")
    public DataResponse addProductToWatchlist(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Product product = new Product();
        product.setProductId(id);
        boolean added = watchlistService.addProductToWatchlist(user, product);
        if (added) {
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .data("Product added to watchlist")
                    .build();
        }
        return DataResponse.builder()
                .success(false)
                .message("Fail")
                .data("Product already in watchlist")
                .build();
    }

    @DeleteMapping("/product/{id}")
    public DataResponse removeProductFromWatchlist(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Product product = new Product();
        product.setProductId(id);
        boolean removed = watchlistService.removeProductFromWatchlist(user, product);
        if (removed) {
            return DataResponse.builder()
                    .success(true)
                    .message("Success")
                    .data("Product removed from watchlist")
                    .build();
        }
        return DataResponse.builder()
                .success(false)
                .message("Fail")
                .data("Product not in watchlist")
                .build();
    }
}