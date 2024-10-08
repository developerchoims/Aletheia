package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestDto {
    @Schema(name = "userId", description = "user id")
    private String userId;
    @Schema(name = "id", description = "주소 id")
    private Long addressId;
    @Schema(name = "transactions", description = "판매")
    private Order.Transactions transactions;

    @Schema(name = "orderDetail", description = "주문 상세 id")
    private List<OrderDetailRequestDto> orderDetail;
}
