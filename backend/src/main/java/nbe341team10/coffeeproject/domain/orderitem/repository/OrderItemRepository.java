package nbe341team10.coffeeproject.domain.orderitem.repository;

import nbe341team10.coffeeproject.domain.orderitem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
