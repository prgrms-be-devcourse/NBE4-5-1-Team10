package nbe341team10.coffeeproject.domain.product.repository;

import nbe341team10.coffeeproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
}
