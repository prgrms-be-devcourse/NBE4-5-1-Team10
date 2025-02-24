package nbe341team10.coffeeproject.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import java.util.List;

@Getter
@NoArgsConstructor
public class CartDto {
    private long id;
    private long userId;
    private List<CartItemDto> cartItems;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDto::new)
                .toList();
    }

}
