package nbe341team10.coffeeproject.domain.product.repository;

import nbe341team10.coffeeproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
}
