package nbe341team10.coffeeproject.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderDetailListResponse {

    @NonNull
    private Long orderId;

    @NonNull
    private LocalDateTime orderDate;

    @NonNull
    private OrderStatus orderStatus;

    @NonNull
    private String firstProductName;

    private String firstProductImageUrl;

    @NonNull
    private int productCategoryCount;

    @NonNull
    private int totalPrice;

    @NonNull
    private String username;

}
