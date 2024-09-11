package com.geumbang.servera.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "`order`", schema = "server-a")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(name = "id", description = "주문 id")
    private Long id;

    @Column(name = "order_number", nullable = false, length = 50)
    @Schema(name = "orderNumber", description = "주문정보(id+주문 시간)-자동생성, human readable")
    private String orderNumber;

    @ColumnDefault("current_timestamp()")
    @Column(name = "order_date")
    @Schema(name = "orderDate", description = "주문 시간", defaultValue = "현재 시간")
    private LocalDateTime orderDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(name = "User", description = "주문한 user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    @Schema(name = "address", description = "주문될 배송지")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(name = "status", description = "주문상태", type="enum")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_chk", nullable = false)
    @Schema(name = "statusChk", description = "판매상태", type="enum")
    private StatusChk statusChk;

    @Enumerated(EnumType.STRING)
    @Column(name = "transactions", nullable = false)
    @Schema(name = "transactions", description = "판매/구매 여부", type = "enum")
    private Transactions transactions;

    @Column(name = "transactions_number")
    @Schema(name = "transactionsNumber", description = "관련 transactionsNumber")
    private String transactionsNumber;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    @Schema(name = "createdAt", description = "생성 시간", defaultValue = "현재 시간")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    @Schema(name = "updatedAt", description = "업데이트된 시간", defaultValue = "업데이트 시 현재 시간")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order")
    @Schema(name = "orderDetails", description = "상세 주문 정보")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @Builder
    public Order(String orderNumber, User user, Address address, Status status
                , StatusChk statusChk, Transactions transactions, String transactionsNumber) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.address = address;
        this.status = status;
        this.statusChk = statusChk;
        this.transactions = transactions;
        this.transactionsNumber = transactionsNumber;
    }

    public enum Status {
        주문완료,
        입금완료,
        발송완료
    }

    public enum StatusChk {
        주문완료,
        송금완료,
        수령완료
    }

    public enum Transactions {
        판매,
        구매
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}