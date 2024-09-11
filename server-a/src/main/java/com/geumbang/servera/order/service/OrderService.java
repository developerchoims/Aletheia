package com.geumbang.servera.order.service;

import com.geumbang.servera.order.model.OrderChkRequestDto;
import com.geumbang.servera.order.model.OrderRequestDto;
import com.geumbang.servera.order.model.OrderResponseDto;
import com.geumbang.servera.order.model.PurchaseRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<String> order(OrderRequestDto order);

    ResponseEntity<String> orderChk(OrderChkRequestDto order);

    ResponseEntity<Page<OrderResponseDto>> orderSelect(String userId, int page, int size);

    ResponseEntity<String> purchase(PurchaseRequestDto purchase);
}
