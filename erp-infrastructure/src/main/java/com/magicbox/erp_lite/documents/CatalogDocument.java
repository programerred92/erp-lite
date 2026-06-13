package com.magicbox.erp_lite.documents;

import com.magicbox.erp_lite.enums.CatalogType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "catalogs")
public class CatalogDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean active;
    private CatalogType catalogType;
    private List<CatalogItem> items;
    private Instant createdAt;
    private Instant updatedAt;
}
