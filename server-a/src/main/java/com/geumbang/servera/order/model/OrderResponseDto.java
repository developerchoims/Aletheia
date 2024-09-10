package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderResponseDto {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Order.Status status;
    private Order.StatusChk statusChk;
    private Long addressId;
    private String address;
    private String zonecode;
    private String addressDetail;

}
