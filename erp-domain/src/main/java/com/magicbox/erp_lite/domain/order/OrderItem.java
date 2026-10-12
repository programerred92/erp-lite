package com.magicbox.erp_lite.domain.order;

import com.magicbox.erp_lite.domain.common.Entity;
import com.magicbox.erp_lite.domain.common.Guard;
import com.magicbox.erp_lite.domain.product.Product;
import com.magicbox.erp_lite.domain.product.ProductId;
import com.magicbox.erp_lite.domain.shared.Money;
import com.magicbox.erp_lite.domain.shared.Quantity;
import lombok.Getter;

/**
 * Order line. Product name and unit price are snapshots taken at order creation.
 */
@Getter
public class OrderItem extends Entity<OrderItemId> {

    private OrderItemId id;
    private ProductId productReference;
    private String productName;
    private Quantity quantity;
    private Money unitPrice;
    private Money subtotal;

    protected OrderItem() {
        super(null);
    }
    public static OrderItem from(Product product, Quantity quantity) {
        Guard.notNull(product, "product");
        Guard.notNull(quantity, "quantity");
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not active: " + product.getSku().value());
        }
        Money unitPrice = product.getPrice();
        if (!unitPrice.isPositive()) {
            throw new IllegalArgumentException("unitPrice must be > 0");
        }
        return new OrderItem(
                OrderItemId.generate(),
                product.getId(),
                product.getName().value(),
                quantity,
                unitPrice,
                unitPrice.multiply(quantity));
    }

    private OrderItem(
            OrderItemId id,
            ProductId productReference,
            String productName,
            Quantity quantity,
            Money unitPrice,
            Money subtotal
    ) {
        super(id);
        this.productReference = productReference;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }
}
