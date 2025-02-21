package nbe341team10.coffeeproject.domain.cart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CartAddProductResponse {
    private long id;
    private long userId;
    private List<CartAddProductCartItemDto> cartItems;

    public CartAddProductResponse(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartAddProductCartItemDto::new)
                .toList();
    }

}
