package nbe341team10.coffeeproject.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import org.springframework.lang.NonNull;

@Getter
@NoArgsConstructor
public class CartItemDto {
    @NonNull
    private long id;
    @NonNull
    private long productId;
    @NonNull
    private String productName;
    private String productImageUrl;
    @NonNull
    private int productStockQuantity;

    @NonNull
    private int quantity;
    @NonNull
    private int price;
    @NonNull
    private int totalPrice;

    CartItemDto(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.productImageUrl = cartItem.getProduct().getImageUrl();
        this.productStockQuantity = cartItem.getProduct().getStockQuantity();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getProduct().getPrice();
        this.totalPrice = cartItem.getProduct().getPrice() * this.quantity;
    }
}
