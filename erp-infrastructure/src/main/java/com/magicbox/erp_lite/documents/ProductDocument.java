package com.magicbox.erp_lite.documents;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_documents")
public class ProductDocument {
    @Id
    private String id;
    private String sku;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
    private BigDecimal price;
    private String currency;
    private Integer stock;
    private Boolean active;
    private String imageUrl;
    /**
     * Dynamic specifications object
     */
    private ProductSpecifications specifications;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
