
package com.bfs.hibernateprojectdemo.domain.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private Integer id;

    private Long userId;

    private int productId;

    private String orderStatus;

    private Date datePlaced;

    private List<OrderItemVO> orderItems = new ArrayList<>();

}