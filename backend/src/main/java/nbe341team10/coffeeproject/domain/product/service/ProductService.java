package nbe341team10.coffeeproject.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.controller.ApiV1CartController;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Product> getItem(long productId) {
        return productRepository.findById(productId);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public Product modify(Long id, String name, String description, int price, String imageUrl, int stockQuantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setStockQuantity(stockQuantity);

        return productRepository.save(product);
    }
}
