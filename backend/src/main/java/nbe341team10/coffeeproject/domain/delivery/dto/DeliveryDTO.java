package nbe341team10.coffeeproject.domain.delivery.dto;

import lombok.*;
import nbe341team10.coffeeproject.domain.order.entity.Orders;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private String deliveryAddress; // 배송 주소 (Orders의 address + postalCode 활용)
    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;

    // Orders 객체를 기반으로 DeliveryDTO 생성
    public static DeliveryDTO fromOrder(Orders order) {
        return DeliveryDTO.builder()
                .orderId(order.getId())
                .deliveryAddress(order.getAddress() + " (" + order.getPostalCode() + ")")
                .deliveryStartDate(LocalDateTime.now()) // 예시로 현재 시간 설정
                .build();
    }

}