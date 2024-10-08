package com.geumbang.servera.order.service.serviceImpl;

import com.geumbang.servera.common.CommonUtil;
import com.geumbang.servera.common.Constants;
import com.geumbang.servera.entity.*;
import com.geumbang.servera.order.model.*;
import com.geumbang.servera.order.service.OrderService;
import com.geumbang.servera.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.EntityTypeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
                    .statusChk(Order.StatusChk.주문완료)
                    .transactions(order.getTransactions())
                    .transactionsNumber(order.getTransactionsNumber())
                    .build();
            orderRepository.save(newOrder);
            orderDetail(order, newOrder);
            orderRepository.updateTransactionsNumber(newOrder.getTransactionsNumber(), newOrder.getOrderNumber());


        } catch (EntityNotFoundException | EntityTypeException e) {

            log.error(e.getMessage(), e);
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


    // purchase data insert(order table)
    @Override
    public ResponseEntity<String> purchase (PurchaseRequestDto purchase) {
        try{
            // userId로 user정보 불러오기
            User user = userRepository.findByUserId(purchase.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(Constants.USER_NOT_FOUND));

            // addressId로 address 정보 불러오기
            Address address = addressRepository.findByIdAndUser(purchase.getAddressId(), purchase.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(Constants.ADDRESS_NOT_FOUND));

            // order 정보에 추가하기
            Order newOrder = Order.builder()
                    .orderNumber(commonUtil.createOrderNumber(purchase.getUserId(), LocalDateTime.now(ZoneId.systemDefault())))
                    .user(user)
                    .address(address)
                    .status(Order.Status.주문완료)
                    .statusChk(Order.StatusChk.주문완료)
                    .transactions(purchase.getTransactions())
                    .build();
            orderRepository.save(newOrder);
            purchaseDetail(purchase, newOrder);

        } catch (EntityNotFoundException | EntityTypeException e) {

            log.error(e.getMessage(),e);
            throw e;

        } catch(Exception e){

            log.error(Constants.PURCHASE_FAIL, e);
            throw new RuntimeException(Constants.PURCHASE_FAIL);

        }
        return ResponseEntity.ok(Constants.PURCHASE_SUCCESS);
    }

    //orderDetail data insert(purchase)
    public void purchaseDetail(PurchaseRequestDto purchase, Order newOrder) {
        //orderNumber로 order 가져오기
        Order savedOrder = orderRepository.findIdByOrderNumber(newOrder.getOrderNumber())
                                            .orElseThrow(()-> new EntityNotFoundException(Constants.ORDER_NOT_FOUND));

        //orderDetail에 저장할 정보 추출
        List<OrderDetailRequestDto> orderDetail = purchase.getOrderDetail();

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



    // 관리자가 order data update (status, statusChk 정보 update)
    @Override
    public ResponseEntity<String> orderChk(OrderChkRequestDto order) {
        // order정보 수정하기
        try{
            orderRepository.updateStatusesById(order.getOrderId(), order.getStatus(), order.getStatusChk());
            orderRepository.updateStatusesByOrderNumber(order.getOrderNumber(), order.getStatus(), order.getStatusChk());
            return ResponseEntity.ok(Constants.ORDER_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(Constants.ORDER_UPDATE_FAIL, e);
            throw new RuntimeException(Constants.ORDER_UPDATE_FAIL);
        }
    }

    // order list select
    // 구매 : 자신의 것만 열람 가능
    // 판매 : 전체 열람 가능, 아이디 검색 가능
    @Override
    public ResponseEntity<Page<OrderResponseDto>> orderSelect(String userId, String search, Order.Transactions transactions, int page, int size) {
        try {
            // id를 통해 주문정보 불러오기
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders;
            // transactions(구매/판매)에 따라 다른 쿼리 실행 시킴
            if(transactions == Order.Transactions.판매) {
                if(search == null || search.isEmpty()){
                    //검색하려는 id가 없고 판매 정보를 열람할 때
                    orders = orderRepository.findAllOrders(transactions, pageable);
                } else {
                    //검색하려는 id가 있고 판매 정보를 열람할 때
                    orders = orderRepository.findAllOrdersBySearchAndTransactions(search, transactions, pageable);
                }
            } else {
                //검색하려는 id 유무에 상관 없이 구매 정보는 본인만 열람 가능
                if(userId == null || userId.isEmpty()){
                    throw new NullPointerException(Constants.USER_NOT_FOUND);
                }
                orders = orderRepository.findAllOrdersByUserIdAndTransactions(userId, transactions, pageable);
            }

            Page<OrderResponseDto> newOrders = orders.map(order -> OrderResponseDto.builder()
                                                                    .orderId(order.getId())
                                                                    .orderNumber(order.getOrderNumber())
                                                                    .orderDate(order.getOrderDate())
                                                                    .status(order.getStatus())
                                                                    .statusChk(order.getStatusChk())
                                                                    .createdAt(order.getCreatedAt())
                                                                    .transactions(order.getTransactions())
                                                                    .transactionsNumber(order.getTransactionsNumber())
                                                                    .addressId(order.getAddress().getId())
                                                                    .address(order.getAddress().getAddress())
                                                                    .addressDetail(order.getAddress().getAddressDetail())
                                                                    .zonecode(order.getAddress().getZonecode())
                                                                    .build()
                                                                );
            // 주문정보 반환
            return ResponseEntity.ok(newOrders);

        } catch(NullPointerException e){
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e){
            log.error(Constants.ORDER_EXCEPTION, e);
            throw new RuntimeException(Constants.ORDER_EXCEPTION);
        }

    }
}
