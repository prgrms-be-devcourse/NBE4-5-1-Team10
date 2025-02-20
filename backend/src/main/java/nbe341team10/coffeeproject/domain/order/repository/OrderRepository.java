package nbe341team10.coffeeproject.domain.order.repository;

import nbe341team10.coffeeproject.domain.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
