package nbe341team10.coffeeproject.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getItems() {
        return productRepository.findAll();
    }

    public long count() {
        return productRepository.count();
    }

    public Product register(String name, String description, int price, String image_url, int stockQuantity) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageUrl(image_url)
                .stockQuantity(stockQuantity)
                .build();

        return productRepository.save(product);

    }

}
