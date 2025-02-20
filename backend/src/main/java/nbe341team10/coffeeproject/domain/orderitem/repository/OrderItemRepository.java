package nbe341team10.coffeeproject.domain.orderitem.repository;

import nbe341team10.coffeeproject.domain.orderitem.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
