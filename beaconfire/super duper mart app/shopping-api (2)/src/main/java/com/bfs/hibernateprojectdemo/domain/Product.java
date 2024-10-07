package com.bfs.hibernateprojectdemo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "wholesale_price")
    private Double wholesalePrice;

    @Column(nullable = false, name = "retail_price")
    private Double retailPrice;

    @Column(nullable = false)
    private Integer quantity;
}