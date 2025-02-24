package nbe341team10.coffeeproject.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderListResponse {

    private Long orderId;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private String firstProductName;

    private int productCategoryCount;

    private int totalPrice;

    private String firstProductImageUrl;

}
