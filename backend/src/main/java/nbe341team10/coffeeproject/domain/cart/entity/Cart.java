package nbe341team10.coffeeproject.domain.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.entity.BaseTime;
import nbe341team10.coffeeproject.global.exception.ServiceException;

import java.util.*;

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

    public CartItem getCartItemByProductId(long productId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(
                        () -> new ServiceException("404-2", "CartItem not found")
                );
    }

    public CartItem addCartItem(Product product, int quantity) {
        CartItem cartItem = CartItem.builder()
                .cart(this)
                .product(product)
                .quantity(quantity)
                .build();

        cartItems.add(cartItem);
        return cartItem;
    }

    public void removeCartItem(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

}
