package nbe341team10.coffeeproject.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.global.entity.BaseTime;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Product extends BaseTime {

    @Column(nullable = false)
    private String name;

    private String description;

    @ColumnDefault("0")
    private int price;

    private String imageUrl;

    @ColumnDefault("0")
    private int stockQuantity;

}
