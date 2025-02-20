package nbe341team10.coffeeproject.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.product.entity.Product;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String image_url;
    private int stockQuantity;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.image_url = product.getImage_url();
        this.stockQuantity = product.getStockQuantity();
    }
}
