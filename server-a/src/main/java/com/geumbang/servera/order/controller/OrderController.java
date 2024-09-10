package com.geumbang.servera.order.controller;

import com.geumbang.servera.order.model.OrderChkRequestDto;
import com.geumbang.servera.order.model.OrderRequestDto;
import com.geumbang.servera.order.model.OrderResponseDto;
import com.geumbang.servera.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    // 소비자의 주문 요청
    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestBody OrderRequestDto order) {
        return orderService.order(order);
    }

    // 판매자가 주문 상태 변경
    @PutMapping("/order")
    public ResponseEntity<String> orderChk(@RequestBody OrderChkRequestDto order) {
        return orderService.orderChk(order);
    }

    // 주문 내역 확인
    @GetMapping("/order")
    public ResponseEntity<Page<OrderResponseDto>> getOrder(@RequestParam String userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return orderService.orderSelect(userId, page, size);
    }
}
