package nbe341team10.coffeeproject.domain.cart.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Optional<Cart> getLatestCart(Long id) {
        return null;
    }

    public Optional<CartItem> getCartItem(Long id, long productId) {
        return null;
    }
}
