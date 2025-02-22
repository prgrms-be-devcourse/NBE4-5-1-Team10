package nbe341team10.coffeeproject.domain.cart.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.repository.CartItemRepository;
import nbe341team10.coffeeproject.domain.cart.repository.CartRepository;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Optional<Cart> getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart getOrCreateCart(Users user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    @Transactional
    public CartItem addProduct(Cart cart, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .map(item -> {
                    item.increaseQuantity(quantity);
                    return item;
                })
                .orElseGet(() -> cart.addCartItem(product, quantity));

        cartItemRepository.save(cartItem);
        return cartItem;
    }

}
