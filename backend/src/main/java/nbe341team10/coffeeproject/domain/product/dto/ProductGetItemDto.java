package nbe341team10.coffeeproject.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetItemDto {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private int price;

    private String imageUrl;

    @NonNull
    private int stockQuantity;

    public ProductGetItemDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = product.getStockQuantity();
    }
}
