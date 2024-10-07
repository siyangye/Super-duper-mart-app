package com.bfs.hibernateprojectdemo.domain;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "watchlist")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {
    @EmbeddedId
    private WatchlistId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embeddable
    @Getter
    @Setter
    public static class WatchlistId implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "product_id")
        private int productId;

        public WatchlistId() {
        }

    }
}