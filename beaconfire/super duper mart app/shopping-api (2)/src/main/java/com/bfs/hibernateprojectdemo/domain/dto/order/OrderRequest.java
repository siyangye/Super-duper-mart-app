package com.bfs.hibernateprojectdemo.domain.dto.order;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<Item> order;

    @Data
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private int productId;
        private int quantity;
    }
}