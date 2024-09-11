package com.geumbang.servera.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "id", description = "user id", type = "UUID")
    private UUID id;

    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$"
            , message = "5~20자리 영어, 숫자만 입력가능합니다.")
    @Column(name = "user_id", nullable = false, length = 20)
    @Schema(name = "userId", description = "로그인 시 사용될 id")
    private String userId;

    @Pattern(regexp = "^[A-Za-z0-9!_]{10,25}"
            , message = "10~25자리의 영어, 숫자, '!', '_' 조합으로 만들어 주세요.")
    @Column(name = "user_pwd", nullable = false, length = 25)
    @Schema(name = "userPwd", description = "로그인 시 사용될 Pwd")
    private String userPwd;

    @Column(name = "user_name", nullable = false, length = 100)
    @Schema(name = "userName", description = "유저 이름")
    private String userName;

    @Column(name = "email", nullable = false, length = 100)
    @Schema(name = "email", description = "유저 이메일")
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    @Schema(name = "phone", description = "유저 핸드폰 번호")
    private String phone;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    @Schema(name = "createdAt", description = "생성 시간", defaultValue = "현재 시간")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    @Schema(name = "updatedAt", description = "업데이트된 시간", defaultValue = "업데이트 시 현재 시간")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ColumnDefault("'회원'")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(name = "role", description = "회원 권한", defaultValue = "회원", type="enum")
    private Role role = Role.회원;

    @OneToMany(mappedBy = "user")
    @Schema(name = "addresses", description = "회원 주소 목록")
    private Set<Address> addresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @Schema(name = "orders", description = "회원 주문 목록")
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