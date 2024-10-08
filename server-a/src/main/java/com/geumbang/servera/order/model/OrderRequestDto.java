package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

// 사용자가 물건 구매시 사용될 DTO
@Data
public class OrderRequestDto {
    @Schema(name = "userId", description = "user id")
    private String userId;
    @Schema(name = "id", description = "주소 id")
    private Long addressId;
    @Schema(name = "transactions", description = "구매")
    private Order.Transactions transactions;
    @Schema(name = "transactionsNumber", description = "구매할 판매주문 번호")
    private String transactionsNumber;

    @Schema(name = "id", description = "주문 상세 id")
    private List<OrderDetailRequestDto> orderDetail;
}
