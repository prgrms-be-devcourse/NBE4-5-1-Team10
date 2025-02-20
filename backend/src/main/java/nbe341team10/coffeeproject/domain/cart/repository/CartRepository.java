package nbe341team10.coffeeproject.domain.cart.repository;

import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
