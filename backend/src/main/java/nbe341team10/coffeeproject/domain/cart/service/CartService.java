package nbe341team10.coffeeproject.domain.cart.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.entity.CartModificationType;
import nbe341team10.coffeeproject.domain.cart.repository.CartItemRepository;
import nbe341team10.coffeeproject.domain.cart.repository.CartRepository;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public Cart updateProduct(Users user, long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be 0 or greater");
        }
        return modifyCart(user, productId, quantity, CartModificationType.UPDATE);
    }

    @Transactional
    public Cart addProduct(Users user, long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return modifyCart(user, productId, quantity, CartModificationType.ADD);
    }

    public Cart getOrCreateCart(Users user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    private Cart modifyCart(Users user, long productId, int quantity, CartModificationType type) {
        Product product = productService.getItem(productId).orElseThrow(
                () -> new EntityNotFoundException("Product with ID %d not found".formatted(productId))
        );

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);


        switch (type) {
            case UPDATE:
                if (quantity < 0) {
                    throw new IllegalArgumentException("Quantity must be 0 or greater");
                } else if (quantity == 0) {
                    existingCartItem.ifPresent( item -> cart.removeCartItem(product.getId()));
                } else {
                    existingCartItem.ifPresentOrElse(
                            item -> item.setQuantity(quantity),
                            () -> cart.addCartItem(product, quantity)
                    );
                }
                break;
            case ADD:
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Quantity must be greater than 0");
                } else {
                    existingCartItem.ifPresentOrElse(
                        item -> item.increaseQuantity(quantity),
                        () -> cart.addCartItem(product, quantity)
                    );
                }
                break;
        }

        cartRepository.flush();
        return cart;
    }
}
