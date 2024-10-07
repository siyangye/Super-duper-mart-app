package com.bfs.hibernateprojectdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="order_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int item_id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "purchased_price")
    private Double purchasedPrice;

    public OrderItem(Order order, Product product, Integer quantity, Double purchasedPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.purchasedPrice = purchasedPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "item_id=" + item_id +
                ", quantity=" + quantity +
                ", purchasedPrice=" + purchasedPrice +
                '}';
    }
}