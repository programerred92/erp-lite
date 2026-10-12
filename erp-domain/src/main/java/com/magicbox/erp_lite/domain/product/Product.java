package com.magicbox.erp_lite.domain.product;

import com.magicbox.erp_lite.domain.common.AggregateRoot;
import com.magicbox.erp_lite.domain.common.Guard;
import com.magicbox.erp_lite.domain.product.events.ProductCreated;
import com.magicbox.erp_lite.domain.product.events.ProductDeactivated;
import com.magicbox.erp_lite.domain.product.events.ProductUpdated;
import com.magicbox.erp_lite.domain.product.events.StockChanged;
import com.magicbox.erp_lite.domain.shared.AuditInfo;
import com.magicbox.erp_lite.domain.shared.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
public class Product extends AggregateRoot<ProductId> {

    private ProductId id;
    private SKU sku;
    private ProductName name;
    private String description;
    private Money price;
    private Stock stock;
    private CategoryReference category;
    private ProductImage image;
    private boolean active;
    private AuditInfo auditInfo;

    Product(ProductId id, SKU sku, ProductName name, String description, Money price, Stock stock,
            CategoryReference category, ProductImage image, boolean active, AuditInfo auditInfo) {
        super(id);
        this.id = Guard.notNull(id, "id");
        this.sku = Guard.notNull(sku, "sku");
        this.name = Guard.notNull(name, "name");
        this.description = description;
        this.price = requirePositivePrice(price);
        this.stock = Guard.notNull(stock, "stock");
        this.category = Guard.notNull(category, "category");
        this.image = image;
        this.active = active;
        this.auditInfo = Guard.notNull(auditInfo, "auditInfo");
    }

    public static Product create(SKU sku, ProductName name, String description, Money price, Stock stock,
                                 CategoryReference category, ProductImage image, String createdBy) {
        Instant now = Instant.now();
        Product product = new Product(ProductId.generate(), sku, name, description, price, stock,
                category, image, true, AuditInfo.create(createdBy, now));
        product.registerEvent(new ProductCreated(product.id, product.sku, product.name, product.price, now));
        return product;
    }

    public void update(ProductName name, String description, Money price, CategoryReference category,
                       ProductImage image) {
        this.name = Guard.notNull(name, "name");
        this.description = description;
        this.price = requirePositivePrice(price);
        this.category = Guard.notNull(category, "category");
        this.image = image;
        touch();
    }

    public void incrementStock(int quantity, String reason) {
        Guard.notBlank(reason, "reason");
        Stock newStock = stock.increment(quantity);
        changeStock(newStock, reason);
    }

    public void decrementStock(int quantity, String reason) {
        Guard.notBlank(reason, "reason");
        Stock newStock = stock.decrement(quantity);
        changeStock(newStock, reason);
    }

    public void changePrice(Money newPrice) {
        this.price = requirePositivePrice(newPrice);
        touch();
    }

    public void deactivate() {
        if (!active) {
            return;
        }
        this.active = false;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductDeactivated(id, auditInfo.updatedAt()));
    }

    public void activate() {
        if (active) {
            return;
        }
        this.active = true;
        touch();
    }

    public boolean hasAvailableStock(int requiredQuantity) {
        return stock.hasAvailable(requiredQuantity);
    }

    private void changeStock(Stock newStock, String reason) {
        int oldValue = stock.value();
        this.stock = newStock;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new StockChanged(id, oldValue, newStock.value(), reason, auditInfo.updatedAt()));
    }

    private void touch() {
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductUpdated(id, auditInfo.updatedAt()));
    }

    private static Money requirePositivePrice(Money price) {
        Guard.notNull(price, "price");
        if (!price.isPositive()) {
            throw new IllegalArgumentException("price must be > 0");
        }
        return price;
    }
}
