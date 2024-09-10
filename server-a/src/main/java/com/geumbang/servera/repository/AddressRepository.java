package com.geumbang.servera.repository;

import com.geumbang.servera.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.id = :id AND a.user.userId = :userId")
    Address findByIdAndUser(Long id, String userId);
}
