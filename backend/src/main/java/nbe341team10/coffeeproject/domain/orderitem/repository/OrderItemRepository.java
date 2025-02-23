package nbe341team10.coffeeproject.domain.orderitem.repository;

import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.orderitem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    int countByOrder(Orders order);

    List<OrderItem> findByOrder(Orders order);
}
