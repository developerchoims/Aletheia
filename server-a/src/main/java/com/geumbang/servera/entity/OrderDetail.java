package com.geumbang.servera.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_detail", schema = "server-a")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(name = "id", description = "주문 상세 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(name = "Order", description = "상위 주문 정보")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id", nullable = false)
    @Schema(name = "item", description = "구입한 아이템")
    private Item item;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    @Schema(name = "quantity", description = "주문량")
    private BigDecimal quantity;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    @Schema(name = "totalPrice", description = "총 주문 금액")
    private BigDecimal totalPrice;

    @Builder
    public OrderDetail(Order order, Item item, BigDecimal quantity, BigDecimal totalPrice) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

}