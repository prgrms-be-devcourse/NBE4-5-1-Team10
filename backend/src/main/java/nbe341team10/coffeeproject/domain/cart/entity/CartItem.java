package nbe341team10.coffeeproject.domain.cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.global.entity.BaseTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"cart_id", "product_id"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CartItem extends BaseTime {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    private int quantity;

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
