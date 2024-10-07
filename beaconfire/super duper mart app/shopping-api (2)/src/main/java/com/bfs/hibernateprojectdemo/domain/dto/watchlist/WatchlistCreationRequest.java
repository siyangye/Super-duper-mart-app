package com.bfs.hibernateprojectdemo.domain.dto.watchlist;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Product;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistCreationRequest {

    @NotNull(message = "User cannot be null")
    User user;

    @NotNull(message = "Product cannot be null")
    Product product;
}