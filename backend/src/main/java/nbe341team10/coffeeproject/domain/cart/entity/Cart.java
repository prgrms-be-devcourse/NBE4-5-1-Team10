package nbe341team10.coffeeproject.domain.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.entity.BaseTime;
import nbe341team10.coffeeproject.global.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Cart extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public CartItem addCartItem(Product product, int quantity) {
        CartItem cartItem = CartItem.builder()
                .cart(this)
                .product(product)
                .quantity(quantity)
                .build();

        cartItems.add(cartItem);
        return cartItem;
    }

    public CartItem getCartItemById(long cartItemId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getId() == cartItemId)
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException("404-2", "CartItem not found")
                );
    }
}
