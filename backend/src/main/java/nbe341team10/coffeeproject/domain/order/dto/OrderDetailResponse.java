package nbe341team10.coffeeproject.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemDetailResponse;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderDetailResponse {

    @NonNull
    private List<OrderItemDetailResponse> orderItems;

    @NonNull
    private OrderStatus orderStatus;

    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String address;

    @NonNull
    private String postalCode;;

    @NonNull
    private int totalPrice;
}
