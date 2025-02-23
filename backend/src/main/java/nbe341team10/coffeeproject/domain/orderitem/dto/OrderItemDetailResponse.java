package nbe341team10.coffeeproject.domain.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderItemDetailResponse {

    private String productName;

    private int quantity;

    private int price;
}
