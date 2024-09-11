package com.geumbang.servera.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "server-a")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(name = "id", description = "주소 id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(name = "User", description = "관련된 User정보")
    private User user;

    @Column(name = "address", nullable = false)
    @Schema(name = "address", description = "주소지")
    private String address;

    @Column(name = "zonecode", nullable = false, length = 20)
    @Schema(name = "zonecode", description = "우편 번호")
    private String zonecode;

    @Column(name = "address_detail", nullable = false)
    @Schema(name = "addressDetail", description = "상세 주소")
    private String addressDetail;

    @ColumnDefault("0")
    @Column(name = "main", nullable = false)
    @Schema(name = "main", description = "메인 주소 여부", defaultValue = "0")
    private Short main;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    @Schema(name = "createdAt", description = "생성 시간", defaultValue = "현재 시간")
    private Instant createdAt;

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    @Schema(name = "updatedAt", description = "업데이트된 시간", defaultValue = "업데이트 시 현재 시간")
    private Instant updatedAt;

}