package nbe341team10.coffeeproject.domain.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@Builder
public class OrderItemDetailResponse {

    @NonNull
    private String productName;

    @NonNull
    private int quantity;

    @NonNull
    private int price;
}
