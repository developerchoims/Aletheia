package com.geumbang.servera.order.model;

import com.geumbang.servera.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class OrderChkRequestDto {
    @Schema(name = "id", description = "주문 id")
    private Long orderId;
    @Schema(name = "status", description = "주문상태", type="enum")
    private Order.Status status;
    @Column(name = "status_chk", nullable = false)
    private Order.StatusChk statusChk;
}
