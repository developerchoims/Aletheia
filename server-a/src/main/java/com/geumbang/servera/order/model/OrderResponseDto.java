package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderResponseDto {
    @Schema(name = "id", description = "주문 id")
    private Long orderId;
    @Schema(name = "orderNumber", description = "주문정보(id+주문 시간)-자동생성, human readable")
    private String orderNumber;
    @Schema(name = "orderDate", description = "주문 시간", defaultValue = "현재 시간")
    private LocalDateTime orderDate;
    @Schema(name = "status", description = "주문상태", type="enum")
    private Order.Status status;
    @Schema(name = "statusChk", description = "판매상태", type="enum")
    private Order.StatusChk statusChk;
    @Schema(name = "created_at", description = "주문 및 판매 날짜")
    private LocalDateTime createdAt;
    @Schema(name = "transactions", description = "판매/구매 여부")
    private Order.Transactions transactions;
    @Schema(name = "transactions_number", description = "관련 transactions number")
    private String transactionsNumber;
    @Schema(name = "id", description = "주소 id")
    private Long addressId;
    @Schema(name = "address", description = "주소지")
    private String address;
    @Schema(name = "zonecode", description = "우편 번호")
    private String zonecode;
    @Schema(name = "addressDetail", description = "상세 주소")
    private String addressDetail;

}
