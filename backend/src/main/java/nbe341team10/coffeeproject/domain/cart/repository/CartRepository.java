package nbe341team10.coffeeproject.domain.cart.repository;

import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
