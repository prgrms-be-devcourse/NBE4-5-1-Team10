package nbe341team10.coffeeproject.domain.delivery.repository;

import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
