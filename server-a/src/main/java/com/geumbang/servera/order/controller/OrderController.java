package com.geumbang.servera.order.controller;

import com.geumbang.servera.order.model.OrderChkRequestDto;
import com.geumbang.servera.order.model.OrderRequestDto;
import com.geumbang.servera.order.model.OrderResponseDto;
import com.geumbang.servera.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "OrderServiceImpl", description = "주문 관련 service입니다.")
public class OrderController {

    private final OrderService orderService;

    // 소비자의 주문 요청
    @Operation(summary = "order_post", description = "주문 정보를 삽입합니다.")
    @Parameter(name = "userId", description = "유저 아이디")
    @Parameter(name = "addressId", description = "주소 아이디")
    @Parameter(name = "orderDetail", description = "주문 상세 정보")
    @ApiResponse(responseCode = "200", description = "주문이 완료되었습니다.")
    @Transactional(propagation = Propagation.REQUIRED)
    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestBody OrderRequestDto order) {
        return orderService.order(order);
    }

    // 판매자가 주문 상태 변경
    @Operation(summary = "order_put", description = "관리자가 주문, 판매 정보를 수정합니다.")
    @Parameter(name = "orderId", description = "주문 아이디")
    @Parameter(name = "status", description = "주문 정보")
    @Parameter(name = "statusChk", description = "판매 정보")
    @ApiResponse(responseCode = "200", description = "주문 상태 변경을 완료하였습니다.")
    @Transactional(propagation = Propagation.REQUIRED)
    @PutMapping("/order")
    public ResponseEntity<String> orderChk(@RequestBody OrderChkRequestDto order) {
        return orderService.orderChk(order);
    }

    // 주문 내역 확인
    @Operation(summary = "order_get", description = "관리자, user가 주문 정보를 확인합니다.")
    @Parameter(name = "userId", description = "유저 아이디")
    @Parameter(name = "page", description = "페이지 정보")
    @Parameter(name = "size", description = "사이즈 정보")
    @ApiResponse(responseCode = "200", description = "Page<Order>")
    @GetMapping("/order")
    public ResponseEntity<Page<OrderResponseDto>> getOrder(@RequestParam String userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return orderService.orderSelect(userId, page, size);
    }
}
