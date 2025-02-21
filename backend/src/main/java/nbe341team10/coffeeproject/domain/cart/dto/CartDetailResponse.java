package nbe341team10.coffeeproject.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import java.util.List;

@Getter
@NoArgsConstructor
public class CartDetailResponse {
    private long id;
    private long userId;
    private List<CartAddProductCartItemDto> cartItems;

    public CartDetailResponse(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartAddProductCartItemDto::new)
                .toList();
    }

}
