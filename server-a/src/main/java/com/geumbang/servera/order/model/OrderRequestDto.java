package com.geumbang.servera.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

// 사용자가 물건 구매시 사용될 DTO
@Data
public class OrderRequestDto {
    @Schema(name = "id", description = "user id", type = "UUID")
    private String userId;
    @Schema(name = "id", description = "주소 id")
    private Long addressId;

    @Schema(name = "id", description = "주문 상세 id")
    private List<OrderDetailRequestDto> orderDetail;
}
