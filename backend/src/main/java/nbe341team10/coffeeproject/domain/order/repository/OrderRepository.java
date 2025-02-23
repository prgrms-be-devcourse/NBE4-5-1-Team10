package nbe341team10.coffeeproject.domain.order.repository;

import nbe341team10.coffeeproject.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
