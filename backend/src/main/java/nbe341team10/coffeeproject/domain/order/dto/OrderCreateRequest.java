package nbe341team10.coffeeproject.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbe341team10.coffeeproject.domain.order.domain.OrderStatus;
import nbe341team10.coffeeproject.domain.order.domain.Orders;

@Getter
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "우편번호를 입력해주세요.")
    private String postalCode;

//    @Size(min = 1, message = "주문 상품을 선택해주세요.")
//    private List<OrderItemCreateRequest> orderItems;
//
    public Orders toOrder(){
//
//        int totalPrice = orderItems.stream()
//                .mapToInt(product -> product.getPrice() * product.getQuantity()) // price * quantity를 계산
//                .sum(); // 모든 값을 합산하여 총 가격 계산


        return Orders.builder()
                .address(address)
                .postalCode(postalCode)
                .status(OrderStatus.ORDERD)
                .totalPrice(1000)
                .build();
    }
}
