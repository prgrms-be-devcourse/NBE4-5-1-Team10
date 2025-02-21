package nbe341team10.coffeeproject.domain.cart.dto;

import lombok.Getter;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;

@Getter
public class CartAddProductCartItemDto {
    private long id;
    private long productId;
    private int quantity;

    CartAddProductCartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.quantity = cartItem.getQuantity();
    }
}
