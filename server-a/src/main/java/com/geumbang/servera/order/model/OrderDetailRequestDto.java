package com.geumbang.servera.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailRequestDto {
    @Schema(name = "id", description = "상품 id")
    private Long itemId;
    @Schema(name = "totalPrice", description = "총 주문 금액")
    private BigDecimal totalPrice;
    @Schema(name = "quantity", description = "주문량")
    private BigDecimal quantity;
}
