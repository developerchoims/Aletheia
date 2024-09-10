package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import lombok.Data;

@Data
public class OrderChkRequestDto {
    private Long orderId;
    private Order.Status status;
    private Order.StatusChk statusChk;
}
