package com.geumbang.servera.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "item", schema = "server-a")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(name = "id", description = "상품 id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Schema(name = "Type", description = "상품 타입", type = "enum")
    private Type type;

    @Column(name = "price_per_gram", nullable = false, precision = 15, scale = 2)
    @Schema(name = "pricePerGram", description = "그램 당 가격")
    private BigDecimal pricePerGram;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    @Schema(name = "createdAt", description = "생성 시간", defaultValue = "현재 시간")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    @Schema(name = "updatedAt", description = "업데이트된 시간", defaultValue = "업데이트 시 현재 시간")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Type {
        GOLD_99_9,
        GOLD_99_99
    }

}