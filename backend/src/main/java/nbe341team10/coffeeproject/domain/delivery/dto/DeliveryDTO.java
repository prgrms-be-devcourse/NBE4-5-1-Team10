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
//    public static DeliveryDTO fromOrder(Orders order) {
//        return DeliveryDTO.builder()
//                .orderId(order.getId())
//                .deliveryAddress(order.getAddress() + " (" + order.getPostalCode() + ")")
//                .deliveryStartDate(LocalDateTime.now()) // 예시로 현재 시간 설정
//                .build();
//    }

    Orders order = new Orders(); // Orders 객체 생성

    DeliveryDTO deliveryDTO = DeliveryDTO.builder()
            .orderId(order.getId()) // Orders 객체에서 ID를 가져옴
            .deliveryAddress(order.getAddress() + " (" + order.getPostalCode() + ")") // 주소와 우편번호를 결합
            .deliveryStartDate(LocalDateTime.now())
            .deliveryArriveDate(LocalDateTime.now().plusDays(1)) // 예시로 1일 후 도착 설정
            .build();



}