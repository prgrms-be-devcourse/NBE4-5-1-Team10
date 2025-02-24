package nbe341team10.coffeeproject.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetItemsDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private int price;

    private String imageUrl;

    @NonNull
    private int stockQuantity;

    public ProductGetItemsDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = product.getStockQuantity();
    }
}
