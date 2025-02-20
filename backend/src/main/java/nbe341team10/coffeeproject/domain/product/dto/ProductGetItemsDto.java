package nbe341team10.coffeeproject.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.product.entity.Product;


@Getter
@AllArgsConstructor
public class ProductGetItemsDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int stockQuantity;

    public ProductGetItemsDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = product.getStockQuantity();
    }
}
