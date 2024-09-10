package com.geumbang.servera.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "server-a")
public class User {
    @Id
    @Column(name = "id", columnDefinition = "uuid not null")
    private UUID id;

    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$"
            , message = "5~20자리 영어, 숫자만 입력가능합니다.")
    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Pattern(regexp = "^[A-Za-z0-9!_]{10,25}"
            , message = "10~25자리의 영어, 숫자, '!', '_' 조합으로 만들어 주세요.")
    @Column(name = "user_pwd", nullable = false, length = 25)
    private String userPwd;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ColumnDefault("'회원'")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.회원;

    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();
    
    public enum Role {
        회원,
        관리자
    }

    @Builder
    public User(String userId, String userName, String phone, Address address){
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.addresses.add(address);
    }

}