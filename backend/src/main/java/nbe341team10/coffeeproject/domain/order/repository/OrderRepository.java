package nbe341team10.coffeeproject.domain.order.repository;

import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser(Users actor);
    List<Orders> findAllByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    int countByStatus(OrderStatus status);
    List<Orders> findTop3ByOrderByCreatedAtDesc();
}
