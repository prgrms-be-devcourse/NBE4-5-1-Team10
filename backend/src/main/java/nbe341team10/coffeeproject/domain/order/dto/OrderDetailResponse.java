package nbe341team10.coffeeproject.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemDetailResponse;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderDetailResponse {

    private List<OrderItemDetailResponse> orderItems;

    private OrderStatus orderStatus;

    private String email;

    private String address;

    private String postalCode;;
}
