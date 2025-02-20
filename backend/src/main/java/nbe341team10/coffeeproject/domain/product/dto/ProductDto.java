package nbe341team10.coffeeproject.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.product.entity.Product;


@Getter
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String imageUrl;
    private int stockQuantity;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = product.getStockQuantity();
    }
}
