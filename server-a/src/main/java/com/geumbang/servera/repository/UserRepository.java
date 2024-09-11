package com.geumbang.servera.repository;

import com.geumbang.servera.entity.Order;
import com.geumbang.servera.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    @Query("SELECT u.id FROM User u  WHERE u.userId = :userId")
    UUID findIdByUserId(String userId);

    @Query("SELECT u.orders FROM User u WHERE u.userId = :userId")
    Page<Order> findAllOrdersByUserId(String userId, Pageable pageable);
}
