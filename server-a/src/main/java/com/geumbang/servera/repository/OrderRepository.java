package com.geumbang.servera.repository;

import com.geumbang.servera.entity.Order;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findIdByOrderNumber(String orderNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.statusChk = :statusChk WHERE o.id = :id")
    int updateStatusesById(Long id, Order.Status status, Order.StatusChk statusChk);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.statusChk = :statusChk WHERE o.transactionsNumber = :orderNumber")
    int updateStatusesByOrderNumber(String orderNumber, Order.Status status, Order.StatusChk statusChk);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.transactionsNumber = :orderNumber WHERE o.orderNumber = :transactionsNumber")
    int updateTransactionsNumber(String transactionsNumber, String orderNumber);


    @Query("SELECT o FROM Order o WHERE o.user.userId = :search AND o.transactions = :transactions AND o.transactionsNumber IS NULL")
    Page<Order> findAllOrdersBySearchAndTransactions(String search, Order.Transactions transactions, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.transactions = :transactions AND o.transactionsNumber IS NULL")
    Page<Order> findAllOrders(Order.Transactions transactions, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.transactions = :transactions AND o.user.userId = :userId")
    Page<Order> findAllOrdersByUserIdAndTransactions(String userId, Order.Transactions transactions, Pageable pageable);

}
