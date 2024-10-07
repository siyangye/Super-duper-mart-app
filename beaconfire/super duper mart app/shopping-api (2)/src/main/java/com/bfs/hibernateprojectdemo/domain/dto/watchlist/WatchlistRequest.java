package com.bfs.hibernateprojectdemo.domain.dto.watchlist;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistRequest {
    private Long userId;
    private int productId;
}