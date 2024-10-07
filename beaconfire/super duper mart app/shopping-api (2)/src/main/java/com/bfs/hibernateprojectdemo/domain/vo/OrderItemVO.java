
package com.bfs.hibernateprojectdemo.domain.vo;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO {
    private int id;

    private int orderId;

    private int productId;

    private Integer quantity;

    private Double purchasedPrice;

}