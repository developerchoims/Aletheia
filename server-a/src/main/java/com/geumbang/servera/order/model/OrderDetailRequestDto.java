package com.geumbang.servera.order.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailRequestDto {
    private Long itemId;
    private BigDecimal totalPrice;
    private BigDecimal quantity;
}
