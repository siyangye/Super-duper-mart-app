package com.bfs.hibernateprojectdemo.domain.vo;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {
    private int productId;

    private String name;

    private String description;

    private Double wholesalePrice;

    private Double retailPrice;

    private Integer quantity;

}