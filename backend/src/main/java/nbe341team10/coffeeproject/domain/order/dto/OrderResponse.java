package nbe341team10.coffeeproject.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponse {

    @NonNull
    private Long orderId;

    @NonNull
    private LocalDateTime orderDate;

    @NonNull
    private OrderStatus orderStatus;

    @NonNull
    private String username;

    @NonNull
    private int totalPrice;

    public OrderResponse(Orders order) {
        this.orderId = order.getId();
        this.orderDate = order.getCreatedAt();
        this.orderStatus = order.getStatus();
        this.username = order.getUser().getUsername();
        this.totalPrice = order.getTotalPrice();
    }

}
