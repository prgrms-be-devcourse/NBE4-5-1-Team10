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

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Optional<Cart> getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Optional<CartItem> getCartItem(Long cartId, long productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Transactional
    public CartItem addProduct(Users user, Product product, int quantity) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                    Cart.builder()
                            .user(user)
                            .build()
                        ));

        CartItem cartItem = cart.addCartItem(product, quantity);

        cartItemRepository.save(cartItem);

        return cartItem;

    }
}
