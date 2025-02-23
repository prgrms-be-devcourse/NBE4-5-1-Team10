package nbe341team10.coffeeproject.domain.delivery.repository;

import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByStatus(String s);
}
