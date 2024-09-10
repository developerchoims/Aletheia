package com.geumbang.servera.order.service.serviceImpl;

import com.geumbang.servera.common.CommonUtil;
import com.geumbang.servera.common.Constants;
import com.geumbang.servera.entity.*;
import com.geumbang.servera.order.model.OrderChkRequestDto;
import com.geumbang.servera.order.model.OrderDetailRequestDto;
import com.geumbang.servera.order.model.OrderRequestDto;
import com.geumbang.servera.order.model.OrderResponseDto;
import com.geumbang.servera.order.service.OrderService;
import com.geumbang.servera.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.EntityTypeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ItemRepository itemRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CommonUtil commonUtil;

    //order data insert
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseEntity<String> order(OrderRequestDto order) {
        try {
            // userId로 user정보 불러오기
            User user = userRepository.findByUserId(order.getUserId())
                                        .orElseThrow(() -> new EntityNotFoundException(Constants.USER_NOT_FOUND));

            // addressId로 address 정보 불러오기
            Address address = addressRepository.findByIdAndUser(order.getAddressId(), order.getUserId())
                                                .orElseThrow(() -> new EntityNotFoundException(Constants.ADDRESS_NOT_FOUND));

            // order 정보에 추가하기
            Order newOrder = Order.builder()
                    .orderNumber(commonUtil.createOrderNumber(order.getUserId(), LocalDateTime.now(ZoneId.systemDefault())))
                    .user(user)
                    .address(address)
                    .status(Order.Status.주문완료)
                    .statusChk(Order.StatusChk.주문완료).build();

            orderRepository.save(newOrder);
            orderDetail(order, newOrder);

        } catch (EntityNotFoundException | EntityTypeException e) {

            throw e;

        } catch(Exception e){

            log.error(Constants.ORDER_FAIL, e);
            throw new RuntimeException(Constants.ORDER_FAIL);

        }
        return ResponseEntity.ok(Constants.ORDER_SUCCESS);
    }

    //orderDetail data insert
    public void orderDetail(OrderRequestDto order, Order newOrder) {
        // orderNumber로 order 가져오기
        Order savedOrder = orderRepository.findIdByOrderNumber(newOrder.getOrderNumber())
                                            .orElseThrow(()-> new EntityNotFoundException(Constants.ORDER_NOT_FOUND));

        //orderDetail에 저장할 정보 추출
        List<OrderDetailRequestDto> orderDetail = order.getOrderDetail();

        //새롭게 저장될 orderDetail
        List<OrderDetail> newOrderDetail = new ArrayList<>();

        // for문 돌며 newOrderDetail에 orderDetail data 저장
        for(OrderDetailRequestDto orderDetailDto : orderDetail) {
            //quantity가 소수점 2번째 자리까지 처리되는 규칙을 위반할 경우 예외 처리
            if (orderDetailDto.getQuantity().scale() > 2) {
                throw new EntityNotFoundException(Constants.QUANTITY_ERROR);
            }

            // itemId로 item 정보 불러오기
            Item item = itemRepository.findById(orderDetailDto.getItemId())
                                        .orElseThrow(()-> new EntityNotFoundException(Constants.ITEM_NOT_FOUND));

            OrderDetail orderDetailForSave = OrderDetail.builder()
                                                        .order(savedOrder)
                                                        .item(item)
                                                        .quantity(orderDetailDto.getQuantity())
                                                        .totalPrice(orderDetailDto.getTotalPrice())
                                                        .build();
            newOrderDetail.add(orderDetailForSave);
        }
        orderDetailRepository.saveAll(newOrderDetail);
    }

    // 관리자가 order data update (status 정보 update)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseEntity<String> orderChk(OrderChkRequestDto order) {
        // order정보 수정하기
        try{
            orderRepository.updateStatusesById(order.getOrderId(), order.getStatus(), order.getStatusChk());
            return ResponseEntity.ok("주문 상태 변경을 완료하였습니다.");
        } catch (Exception e) {
            log.error(Constants.ORDER_UPDATE_FAIL, e);
            throw new RuntimeException(Constants.ORDER_UPDATE_FAIL);
        }
    }

    // order list select(관리자, 유저 공통)
    // 관리자 - userId : 검색 input value 값 - /api/admin/order (get) 에서 사용
    // 유 저 - userId : 유저 본인의 id - /api/order (get) 에서 사용
    @Override
    public ResponseEntity<Page<OrderResponseDto>> orderSelect(String userId, int page, int size) {
        try {
            // user의 Id 가져오기
            UUID id = userRepository.findIdByUserId(userId);

            // id를 통해 주문정보 불러오기
            Pageable pageable = PageRequest.of(page, size);
            Page<Optional<Order>> orders = orderRepository.findAllByUserId(id, pageable);
            Page<OrderResponseDto> newOrders = orders.map(order -> OrderResponseDto.builder()
                                                                    .orderId(order.get().getId())
                                                                    .orderNumber(order.get().getOrderNumber())
                                                                    .orderDate(order.get().getOrderDate())
                                                                    .status(order.get().getStatus())
                                                                    .statusChk(order.get().getStatusChk())
                                                                    .addressId(order.get().getAddress().getId())
                                                                    .address(order.get().getAddress().getAddress())
                                                                    .addressDetail(order.get().getAddress().getAddressDetail())
                                                                    .zonecode(order.get().getAddress().getZonecode())
                                                                    .build()
                                                                );
            // 주문정보 반환
            return ResponseEntity.ok(newOrders);

        } catch (Exception e){
            log.error(Constants.ORDER_EXCEPTION, e);
            throw new RuntimeException(Constants.ORDER_EXCEPTION);
        }

    }
}
