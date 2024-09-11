package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestDto {
    @Schema(name = "userId", description = "user id")
    private String userId;
    @Schema(name = "id", description = "林家 id")
    private Long addressId;
    @Schema(name = "transactions", description = "魄概")
    private Order.Transactions transactions;

    @Schema(name = "orderDetail", description = "林巩 惑技 id")
    private List<OrderDetailRequestDto> orderDetail;
}
