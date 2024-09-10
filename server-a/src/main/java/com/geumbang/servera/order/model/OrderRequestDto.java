package com.geumbang.servera.order.model;

import lombok.Data;

import java.util.List;

// 사용자가 물건 구매시 사용될 DTO
@Data
public class OrderRequestDto {
    private String userId;
    private Long addressId;

    private List<OrderDetailRequestDto> orderDetail;
}
