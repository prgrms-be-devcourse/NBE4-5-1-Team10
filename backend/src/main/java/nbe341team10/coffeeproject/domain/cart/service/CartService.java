package nbe341team10.coffeeproject.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.repository.CartItemRepository;
import nbe341team10.coffeeproject.domain.cart.repository.CartRepository;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductService productService;
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
    public Cart updateProduct(Users user, long productId, int quantity) {
        Product product = productService.getItem(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with ID %d not found".formatted(productId))
        );
        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be 0 or greater");
        } else if (quantity == 0) {
            existingCartItem.ifPresent( item -> {
                cart.removeCartItem(product);
            });
        } else if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(quantity);
        } else {
            cart.addCartItem(product, quantity);
        }

        cartRepository.flush();
        return cart;
    }
}
