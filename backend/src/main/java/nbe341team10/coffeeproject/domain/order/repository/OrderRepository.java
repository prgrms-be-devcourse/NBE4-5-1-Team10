package nbe341team10.coffeeproject.domain.order.repository;

import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser(Users actor);
}
