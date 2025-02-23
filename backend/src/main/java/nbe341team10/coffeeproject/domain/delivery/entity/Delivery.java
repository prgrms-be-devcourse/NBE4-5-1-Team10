package nbe341team10.coffeeproject.domain.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.global.entity.BaseTime;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery extends BaseTime {

    @ManyToOne
    private Orders order;

    private String deliveryAddress;

    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;

    // 배송 상태를 나타내는 필드 추가
    @Enumerated(EnumType.STRING) // 열거형으로 저장
    private OrderStatus status;

    // 배송 상태를 업데이트하는 메서드
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
