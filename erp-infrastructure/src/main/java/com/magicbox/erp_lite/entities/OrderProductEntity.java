package com.magicbox.erp_lite.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "order_products",
        indexes = {
                @Index(name = "idx_order_products_order", columnList = "order_id"),
                @Index(name = "idx_order_products_product", columnList = "product_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_order_products_order_product", columnNames = {"order_id", "product_id"})
        }
)
@BatchSize(size = 50)
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_products_order")
    )
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_products_product")
    )
    private ProductEntity product;

    @NotBlank
    @Size(max = 200)
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Digits(integer = 15, fraction = 2)
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @NotNull
    @Digits(integer = 15, fraction = 2)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    void prePersist() {
        if (unitPrice != null && quantity != null && subtotal == null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()));
        }
        if (product != null && (productName == null || productName.isBlank())) {
            productName = product.getName();
        }
    }
}
